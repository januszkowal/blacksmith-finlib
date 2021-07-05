package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.fxccypair.FxCurrencyPair;
import org.blacksmith.finlib.rate.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rate.fxrate.FxRate;
import org.blacksmith.finlib.rate.fxrate.FxRate3;
import org.blacksmith.finlib.rate.fxrate.FxRateId;
import org.blacksmith.finlib.rate.fxrate.FxRateService;
import org.blacksmith.finlib.rate.fxrate.FxRateType;
import org.blacksmith.finlib.marketdata.MarketDataExtractor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FxRateServiceImpl implements FxRateService {

  private final Currency localCurrency;
  private final FxCurrencyPairProvider ccyPairProvider;
  private final MarketDataProvider<FxRateId, FxRate3> fxRateProvider;
  private final int decimalPlaces;

  public FxRateServiceImpl(Currency localCurrency, FxCurrencyPairProvider ccyPairProvider, MarketDataProvider<FxRateId, FxRate3> fxRateProvider,
      int decimalPlaces) {
    ArgChecker.notNull(localCurrency, "Local currency must be not null");
    ArgChecker.notNull(ccyPairProvider, "Currency Pair Provider must be not null");
    ArgChecker.notNull(fxRateProvider, "Fx Rate Provider must be not null");
    this.localCurrency = localCurrency;
    this.ccyPairProvider = ccyPairProvider;
    this.fxRateProvider = fxRateProvider;
    this.decimalPlaces = decimalPlaces;
  }

  @Override
  public FxRate fxRate(FxRateId key, LocalDate date, FxRateType fxRateType) {
    ArgChecker.notNull(key, "Key must be not null");
    ArgChecker.notNull(date, "Date must be not null");
    ArgChecker.notNull(fxRateType, "Type must be not null");
    if (key.getBaseCcy().equals(key.getCounterCcy())) {
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
  public FxRate3 fxRate3(FxRateId key, LocalDate date) {
    ArgChecker.notNull(key, "Key must be not null");
    ArgChecker.notNull(date, "Date must be not null");
    if (key.getBaseCcy().equals(key.getCounterCcy())) {
      return FxRate3.of(date, Rate.ONE, Rate.ONE, Rate.ONE);
    }
    return getRateInternal(key, date, md -> FxRate3Internal.of(md.getDate(), md.getValue())).toFxRate3(decimalPlaces);
  }

  private FxCurrencyPairInternal getPairInternal(FxRateId key) {
    FxCurrencyPair pair;
    FxRateId actualKey = key;
    if ((pair = getPair(key)) == null) {
      actualKey = key.inverse();
      if ((pair = getPair(actualKey)) == null) {
        throw new IllegalArgumentException("Unknown pair " + key.getPairName());
      }
      return new FxCurrencyPairInternal(actualKey, pair.isCross(), false, pair.getFactor());
    } else {
      return new FxCurrencyPairInternal(actualKey, pair.isCross(), true, pair.getFactor());
    }
  }

  private FxCurrencyPair getPair(FxRateId key) {
    return this.ccyPairProvider.getPair(key.getBaseCcy(), key.getCounterCcy());
  }

  private <R extends FxRateOperations<R>> Optional<R> getSourceFxRate(FxRateId key, LocalDate date,
      Function<FxRate3, R> extractor) {
    return Optional.ofNullable(fxRateProvider.getValue(key, date))
        .map(extractor);
  }

  private <R extends FxRateOperations<R>> R getRateInternal(FxRateId key, LocalDate date,
      Function<FxRate3, R> extractor) {
    R result;
    FxCurrencyPairInternal pair = getPairInternal(key);
    if (pair.isCross()) {
      result = getCrossRate(key, date, extractor);
    } else {
      result = getSimpleRate(pair, date, extractor);
    }
    return result;
  }

  private <R extends FxRateOperations<R>> R getSimpleRate(FxCurrencyPairInternal pair, LocalDate date,
      Function<FxRate3, R> extractor) {
    Optional<R> rateSrc = getSourceFxRate(pair.getFxRateId(), date, extractor);
    if (rateSrc.isEmpty()) {
      throw new IllegalArgumentException(String.format("No available rate %s on %s", pair.getFxRateId().getPairName(),
          date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
    }
    R rate = rateSrc.get();
    log.debug("Simple rate pair={}", pair);
    if (pair.isDirect()) {
      return rate.divide(pair.getFactor());
    } else {
      return rate.inverse2(pair.getFactor());
    }
  }

  private <R extends FxRateOperations<R>> R getCrossRate(FxRateId key, LocalDate date,
      Function<FxRate3, R> extractor) {
    FxRateId key1 = FxRateId.of(key.getBaseCcy(), localCurrency);
    FxRateId key2 = FxRateId.of(key.getCounterCcy(), localCurrency);
    FxCurrencyPairInternal pair1 = getPairInternal(key1);
    FxCurrencyPairInternal pair2 = getPairInternal(key2);
    Optional<R> r1 = getSourceFxRate(pair1.getFxRateId(), date, extractor);
    Optional<R> r2 = getSourceFxRate(pair2.getFxRateId(), date, extractor);
    log.debug("Cross rate pair={}", key);
    log.debug("Pair1={}", pair1);
    log.debug("Pair2={}", pair2);
    if (r1.isPresent() && r2.isPresent()) {
      R result;
      R r1v = r1.get();
      R r2v = r2.get();
      if (pair1.isDirect() && pair2.isDirect()) {
        double factor = pair2.getFactor() / pair1.getFactor();
        result = r1v.multiplyAndDivide(factor, r2v);
      } else if (pair1.isDirect() && !pair2.isDirect()) {
        double factor = pair1.getFactor() * pair2.getFactor();
        result = r1v.multiplyAndDivide(r2v, factor);
      } else if (!pair1.isDirect() && pair2.isDirect()) {
        double factor = pair1.getFactor() * pair2.getFactor();
        result = r1v.inverse2(factor, r2v);
      } else {
        double factor = pair1.getFactor() / pair2.getFactor();
        result = r2v.multiplyAndDivide(factor, r1v);
      }
      return result;
    } else {
      if (r1.isEmpty()) {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair1.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
      if (r2.isEmpty()) {
        throw new IllegalArgumentException(String.format("No available rate %s on %s", pair2.getFxRateId().getPairName(),
            date.format(DateTimeFormatter.ISO_LOCAL_DATE)));
      }
    }
    return null;
  }

  private Function<FxRate3, FxRate1Internal> fx3toFx1b(FxRateType fxRateType) {
    return fxRate3 -> FxRate1Internal.of(fxRateType.getRateExtractor().apply(fxRate3).doubleValue(), fxRate3.getDate());
  }
}
