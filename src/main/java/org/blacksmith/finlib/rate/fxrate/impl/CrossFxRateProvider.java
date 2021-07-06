package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;
import org.blacksmith.finlib.rate.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rate.fxrate.FxRate3;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrossFxRateProvider implements FxRateProvider {

  private final Currency localCurrency;
  private final FxCurrencyPairProvider ccyPairProvider;
  private final MarketDataProvider<FxRateId, FxRate3> sourceRateProvider;

  public CrossFxRateProvider(Currency localCurrency, FxCurrencyPairProvider ccyPairProvider, MarketDataProvider<FxRateId, FxRate3> sourceRateProvider) {
    this.localCurrency = localCurrency;
    this.ccyPairProvider = ccyPairProvider;
    this.sourceRateProvider = sourceRateProvider;
  }
  @Override
  public <R extends FxRateOperations<R>> R rate(CurrencyPairExt pair, LocalDate date, Function<FxRate3, R> extractor) {
    FxRateId key1 = FxRateId.of(pair.isDirect() ? pair.getBase() : pair.getCounter(), localCurrency);
    FxRateId key2 = FxRateId.of(pair.isDirect() ? pair.getCounter() : pair.getBase(), localCurrency);
    CurrencyPairExt pair1 = ccyPairProvider.getPair2Dir(key1);
    CurrencyPairExt pair2 = ccyPairProvider.getPair2Dir(key2);
    log.debug("Cross rate pair={} pair2={} pair2={}", pair, pair1, pair2);
    return calcRate(pair1, pair2, date, extractor);
  }

  private <R extends FxRateOperations<R>> R calcRate(CurrencyPairExt pair1, CurrencyPairExt pair2, LocalDate date, Function<FxRate3, R> extractor) {
    Optional<FxRate3> r1 = sourceRateProvider.value(pair1.getFxRateId(), date);
    Optional<FxRate3> r2 = sourceRateProvider.value(pair2.getFxRateId(), date);
    if (r1.isPresent() && r2.isPresent()) {
      R result;
      R r1v = r1.map(extractor::apply).get();
      R r2v = r2.map(extractor::apply).get();

      if (pair1.isDirect() && pair2.isDirect()) {
        double factor = pair2.getFactor() / pair1.getFactor();
        result = r1v.multiplyAndDivide(factor, r2v);
      } else if (pair1.isDirect() && !pair2.isDirect()) {
        double factor = pair1.getFactor() * pair2.getFactor();
        result = r1v.multiplyAndDivide(r2v, factor);
      } else if (!pair1.isDirect() && pair2.isDirect()) {
        double factor = pair1.getFactor() * pair2.getFactor();
        result = r1v.multiplyAndInverse(r2v, factor);
      } else {
        double factor = pair1.getFactor() / pair2.getFactor();
        result = r2v.multiplyAndDivide(factor, r1v);
      }
      return result;
    }
    else {
      if (r1.isEmpty()) {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair1.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
      else  {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair2.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
    }
  }
}
