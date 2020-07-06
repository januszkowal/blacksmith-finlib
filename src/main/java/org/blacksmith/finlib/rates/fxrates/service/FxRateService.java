package org.blacksmith.finlib.rates.fxrates.service;

import java.time.LocalDate;
import java.util.Optional;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRate1;
import org.blacksmith.finlib.rates.fxrates.FxRate3;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateType;

public interface FxRateService {
  FxRate1 getRate(FxRateId key, LocalDate date, FxRateType fxRateType);
  FxRate3 getRate3(FxRateId key, LocalDate date);

  default Rate getRateValue(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(getRate(key,date,fxRateType))
        .map(FxRate1::getValue)
        .orElse(null);
  }

  default double getRateDouble(FxRateId key, LocalDate date, FxRateType fxRateType) {
    return Optional.ofNullable(getRate(key,date,fxRateType))
        .map(FxRate1::getValue)
        .map(Rate::doubleValue)
        .orElse(0.0d);
  }
}
