package org.blacksmith.finlib.rate.fxrate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.marketdata.MarketDataExtractor;

public interface FxRateService {
  FxRate fxRate(FxRateId pair, LocalDate date, FxRateType fxRateType);

  <V, R> R fxRate(FxRateId pair, LocalDate date, MarketDataExtractor<FxRate3, R> extractor);

  FxRate3 fxRate3(FxRateId pair, LocalDate date);

  default double convert(double amount, FxRateId pair, LocalDate date, FxRateType fxRateType) {
    double rate = fxRate(pair, date, fxRateType).getValue().doubleValue();
    return amount * rate;
  }

  default double fxRateDouble(FxRateId pair, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(fxRate(pair, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::doubleValue)
        .orElse(0d);
  }

  default BigDecimal fxRateBigDecimal(FxRateId pair, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(fxRate(pair, date, fxRateType))
        .map(FxRate::getValue)
        .map(Rate::getValue)
        .orElse(BigDecimal.ZERO);
  }
}
