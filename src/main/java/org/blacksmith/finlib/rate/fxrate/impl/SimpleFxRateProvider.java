package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.blacksmith.finlib.marketdata.MarketDataProvider;
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
  public <R extends FxRateOperations<R>> R rate(CurrencyPairExt2 pair, LocalDate date, Function<FxRate3, R> extractor) {
    FxRate3 rate = sourceRateProvider.get(pair.getFxRateId(), date);
    if (rate == null) {
      throw new IllegalArgumentException(String.format("No available rate %s on %s", pair.getFxRateId().getPairName(),
          date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }
    log.debug("Simple rate pair={}", pair);
    if (pair.isDirect()) {
      return extractor.apply(rate).divide(pair.getFactor());
    } else {
      return extractor.apply(rate).inverse2(pair.getFactor());
    }
  }
}
