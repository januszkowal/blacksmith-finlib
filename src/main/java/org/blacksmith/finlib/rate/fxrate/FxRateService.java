package org.blacksmith.finlib.rate.fxrate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketDataExtractor;

public interface FxRateService {
  FxRate fxRate(FxRateId key, LocalDate date, FxRateType fxRateType);

  <V, R> R fxRate(FxRateId key, LocalDate date, MarketDataExtractor<FxRate3, R> extractor);

  FxRate3 fxRate3(FxRateId key, LocalDate date);

  default double convert(double amount, FxRateId key, LocalDate date, FxRateType fxRateType) {
    double rate = fxRate(key, date, fxRateType).getValue().doubleValue();
    return amount * rate;
  }


//  default Rate fxRate(FxRateId key, LocalDate date, FxRateType fxRateType) {
//    return Optional.ofNullable(getRate(key, date, fxRateType))
//        .map(FxRate::getValue)
//        .orElse(null);
//  }

  default double fxRateDouble(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(fxRate(key, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::doubleValue)
        .orElse(0d);
  }

  default BigDecimal fxRateBigDecimal(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(fxRate(key, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::getValue)
        .orElse(BigDecimal.ZERO);
  }
}
