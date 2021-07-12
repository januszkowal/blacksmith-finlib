package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;
import org.blacksmith.finlib.rate.fxrate.FxRate3;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleFxRateProvider implements FxRateProvider {

  private final MarketDataProvider<FxRateId, FxRate3> sourceRateProvider;

  public SimpleFxRateProvider(MarketDataProvider<FxRateId, FxRate3> sourceRateProvider) {
    this.sourceRateProvider = sourceRateProvider;
  }

  @Override
  public <R extends FxRateOperations<R>> R rate(CurrencyPairExt pair, LocalDate date, Function<FxRate3, R> extractor) {
    Optional<FxRate3> rate = sourceRateProvider.value(pair.getFxRateId(), date);
    if (rate.isEmpty()) {
      throw new IllegalArgumentException(String.format("No available rate %s on %s", pair.getFxRateId().getPairName(),
          date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }
    log.debug("Simple rate pair={}", pair);
    if (pair.isDirect()) {
      return rate.map(extractor::apply).get().divide(pair.getFactor());
    } else {
      return rate.map(extractor::apply).get().inverse2(pair.getFactor());
    }
  }
}
