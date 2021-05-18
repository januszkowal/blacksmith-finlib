package org.blacksmith.finlib.rates.fxrates.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.MarketData;
import org.blacksmith.finlib.rates.MarketDataService;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPair;
import org.blacksmith.finlib.rates.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rates.fxrates.FxRate;
import org.blacksmith.finlib.rates.fxrates.FxRate3;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateOperations;
import org.blacksmith.finlib.rates.fxrates.FxRateType;

public class FxRateServiceImpl implements FxRateService {

  private final Currency localCurrency;
  private final FxCurrencyPairProvider ccyPairProvider;
  private final MarketDataService<FxRateId, FxRate3.FxRate3Values> fxRateSourceService;
  private final int decimalPlaces;
  private final int outputDecimalPlaces;

  public FxRateServiceImpl(FxCurrencyPairProvider ccyPairProvider,
      MarketDataService<FxRateId, FxRate3.FxRate3Values> fxRateSourceService,
      Currency localCurrency, int decimalPlaces, int outputDecimalPlaces) {
    ArgChecker.notNull(ccyPairProvider, "CcyPairProvider must be not null");
    ArgChecker.notNull(fxRateSourceService, "FxRatSource must be not null");
    ArgChecker.notNull(localCurrency, "Local currency must be not null");
    this.ccyPairProvider = ccyPairProvider;
    this.fxRateSourceService = fxRateSourceService;
    this.localCurrency = localCurrency;
    this.decimalPlaces = decimalPlaces;
    this.outputDecimalPlaces = outputDecimalPlaces;
  }

  @Override
  public FxRate getRate(FxRateId key, LocalDate date, FxRateType fxRateType) {
    ArgChecker.notNull(key);
    ArgChecker.notNull(date);
    ArgChecker.notNull(fxRateType);
    if (key.getFromCcy().equals(key.getToCcy())) {
      return FxRate.of(date, Rate.ONE);
    }
    return getRateInternal(key, date, fxRateType::toFxRate);
  }

  @Override
  public FxRate3 getRate3(FxRateId key, LocalDate date) {
    ArgChecker.notNull(key);
    ArgChecker.notNull(date);
    if (key.getFromCcy().equals(key.getToCcy())) {
      return FxRate3.of(date, Rate.ONE, Rate.ONE, Rate.ONE);
    }
    return getRateInternal(key, date, r3 -> r3);
  }

  private FxCurrencyPair getPair(FxRateId key) {
    return this.ccyPairProvider.getPair(key.getFromCcy().getCurrencyCode(), key.getToCcy().getCurrencyCode());
  }

  private FxRate3 getSourceFxRate(FxRateId key, LocalDate date) {
    return Optional.ofNullable(fxRateSourceService.getRate(key, date))
        .map(r -> FxRate3.of(r.getDate(), r.getValue(), decimalPlaces))
        .orElse(null);
  }

  private <R extends FxRateOperations<R>> R getSimpleRate(FxRateId key, LocalDate date, FxCurrencyPair pair,
      Function<FxRate3, R> extractor, boolean inverse) {
    FxRate3 rateSrc = getSourceFxRate(key, date);
    if (rateSrc == null)
      return null;
    R rate = extractor.apply(rateSrc).divide(pair.getFactor(), outputDecimalPlaces);
    return (inverse) ? rate.inverse() : rate;
  }

  private <R extends FxRateOperations<R>> R getRateInternal(FxRateId key, LocalDate date, Function<FxRate3, R> extractor) {
    FxRateId rateKey = key;
    FxCurrencyPair pair;
    boolean inverse = false;
    R result = null;
    if ((pair = getPair(rateKey)) == null) {
      inverse = true;
      rateKey = rateKey.inverse();
      if ((pair = getPair(rateKey)) == null) {
        throw new IllegalArgumentException("Unknown pair " + key.getPairName());
      }
    }
    if (pair.isCross()) {
      result = getCrossRate(rateKey, date, extractor, inverse);
    } else {
      result = getSimpleRate(rateKey, date, pair, extractor, inverse);
    }
    return result;
  }

  private <R extends FxRateOperations<R>> R getCrossRate(FxRateId key, LocalDate date, Function<FxRate3, R> extractor, boolean inverse) {
    FxRateId k1 = FxRateId.of(key.getFromCcy(), localCurrency);
    FxRateId k2 = FxRateId.of(key.getToCcy(), localCurrency);
    FxCurrencyPair p1 = getPair(k1);
    FxCurrencyPair p2 = getPair(k2);
    if (p1 == null) {
      throw new IllegalArgumentException("Unknown pair " + k1.getPairName());
    }
    if (p2 == null) {
      throw new IllegalArgumentException("Unknown pair " + k2.getPairName());
    }
    FxRate3 r1 = getSourceFxRate(k1, date);
    FxRate3 r2 = getSourceFxRate(k2, date);
    if (r1 != null && r2 != null) {
      if (inverse) {
        double factor = p1.getFactor() / p2.getFactor();
        return extractor.apply(r2).crossDivide(factor, extractor.apply(r1), outputDecimalPlaces);
      } else {
        double factor = p2.getFactor() / p1.getFactor();
        return extractor.apply(r1).crossDivide(factor, extractor.apply(r2), outputDecimalPlaces);
      }
    }
    return null;
  }
}
