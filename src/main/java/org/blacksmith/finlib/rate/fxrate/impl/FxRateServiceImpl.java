package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.util.function.Function;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketDataExtractor;
import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;
import org.blacksmith.finlib.rate.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rate.fxrate.FxRate;
import org.blacksmith.finlib.rate.fxrate.FxRate3;
import org.blacksmith.finlib.rate.fxrate.FxRateId;
import org.blacksmith.finlib.rate.fxrate.FxRateService;
import org.blacksmith.finlib.rate.fxrate.FxRateType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FxRateServiceImpl implements FxRateService {

  private final Currency localCurrency;
  private final FxCurrencyPairProvider ccyPairProvider;
  private final int decimalPlaces;
  private final SimpleFxRateProvider simpleRateProvider;
  private final CrossFxRateProvider crossRateProvider;

  public FxRateServiceImpl(Currency localCurrency, FxCurrencyPairProvider ccyPairProvider, MarketDataProvider<FxRateId, FxRate3> fxRateProvider,
      int decimalPlaces) {
    ArgChecker.notNull(localCurrency, "Local currency must be not null");
    ArgChecker.notNull(ccyPairProvider, "Currency Pair Provider must be not null");
    ArgChecker.notNull(fxRateProvider, "Fx Rate Provider must be not null");
    this.localCurrency = localCurrency;
    this.decimalPlaces = decimalPlaces;
    this.ccyPairProvider = ccyPairProvider;
    this.simpleRateProvider = new SimpleFxRateProvider(fxRateProvider);
    this.crossRateProvider = new CrossFxRateProvider(localCurrency, ccyPairProvider, fxRateProvider);
  }

  @Override
  public FxRate fxRate(FxRateId key, LocalDate date, FxRateType fxRateType) {
    ArgChecker.notNull(key, "Key must be not null");
    ArgChecker.notNull(date, "Date must be not null");
    ArgChecker.notNull(fxRateType, "Type must be not null");
    if (key.getBase().equals(key.getCounter())) {
      return FxRate.of(Rate.ONE, date);
    }
    return getRateInternal(key, date, fx3toFx1b(fxRateType)).toFxRate(decimalPlaces);
  }

  @Override
  public <V, R> R fxRate(FxRateId key, LocalDate date, MarketDataExtractor<FxRate3, R> extractor) {
    ArgChecker.notNull(extractor, "Extractor must be not null");
    return extractor.extract(fxRate3(key, date));
  }

  @Override
  public FxRate3 fxRate3(FxRateId pair, LocalDate date) {
    ArgChecker.notNull(pair, "Pair must be not null");
    ArgChecker.notNull(date, "Date must be not null");
    if (pair.getBase().equals(pair.getCounter())) {
      return FxRate3.of(date, Rate.ONE, Rate.ONE, Rate.ONE);
    }
    return getRateInternal(pair, date, md -> FxRate3Internal.of(md.getDate(), md.getValue())).toFxRate3(decimalPlaces);
  }

  private <R extends FxRateOperations<R>> R getRateInternal(FxRateId pair, LocalDate date,
      Function<FxRate3, R> extractor) {
    R result;
    CurrencyPairExt pairExt = ccyPairProvider.getPair2Dir(pair);
    if (pairExt.isCross()) {
      result = crossRateProvider.rate(pairExt, date, extractor);
    } else {
      result = simpleRateProvider.rate(pairExt, date, extractor);
    }
    return result;
  }

  private Function<FxRate3, FxRate1Internal> fx3toFx1b(FxRateType fxRateType) {
    return fxRate3 -> FxRate1Internal.of(fxRateType.getRateExtractor().apply(fxRate3).doubleValue(), fxRate3.getDate());
  }
}
