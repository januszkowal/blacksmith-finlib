package org.blacksmith.finlib.rate.fxrate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.MarketDataExtractor;

public interface FxRateService {
  FxRate getRate(FxRateId key, LocalDate date, FxRateType fxRateType);

  <V, R> R getRate(FxRateId key, LocalDate date, MarketDataExtractor<FxRate3.FxRate3Data, R> extractor);

  FxRate3 getRate(FxRateId key, LocalDate date);


  default Rate getRateValue(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(getRate(key, date, fxRateType))
        .map(FxRate::getValue)
        .orElse(null);
  }

  default double getRateDouble(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(getRate(key, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::doubleValue)
        .orElse(0d);
  }

  default BigDecimal getRateDecimal(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(getRate(key, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::getValue)
        .orElse(BigDecimal.ZERO);
  }
}