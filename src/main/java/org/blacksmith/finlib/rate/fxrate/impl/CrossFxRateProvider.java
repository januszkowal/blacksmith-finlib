package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.fxrate.FxRate3;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrossFxRateProvider implements FxRateProvider {

  private final Currency localCurrency;
  private final FxCurrencyPairProviderImpl ccyPairProvider;
  private final MarketDataProvider<FxRateId, FxRate3> sourceRateProvider;

  public CrossFxRateProvider(Currency localCurrency, FxCurrencyPairProviderImpl ccyPairProvider, MarketDataProvider<FxRateId, FxRate3> sourceRateProvider) {
    this.localCurrency = localCurrency;
    this.ccyPairProvider = ccyPairProvider;
    this.sourceRateProvider = sourceRateProvider;
  }
  @Override
  public <R extends FxRateOperations<R>> R rate(CurrencyPairExt2 pair, LocalDate date, Function<FxRate3, R> extractor) {
    FxRateId key1 = FxRateId.of(pair.isDirect() ? pair.getBase() : pair.getCounter(), localCurrency);
    FxRateId key2 = FxRateId.of(pair.isDirect() ? pair.getCounter() : pair.getBase(), localCurrency);
    CurrencyPairExt2 pair1 = ccyPairProvider.getPairExt(key1);
    CurrencyPairExt2 pair2 = ccyPairProvider.getPairExt(key2);
    FxRate3 r1 = sourceRateProvider.get(pair1.getFxRateId(), date);
    FxRate3 r2 = sourceRateProvider.get(pair2.getFxRateId(), date);
    log.debug("Cross rate pair={} pair2={} pair2={}", pair, pair1, pair2);
    if (r1 != null && r2 != null) {
      R result;
      R r1v = extractor.apply(r1);
      R r2v = extractor.apply(r2);

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
    } else {
      if (r1 == null) {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair1.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
      if (r2 == null) {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair2.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
    }
    return null;
  }
}
