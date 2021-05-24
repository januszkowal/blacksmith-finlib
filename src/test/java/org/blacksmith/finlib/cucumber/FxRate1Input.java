package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateType;

import lombok.Value;

@Value(staticConstructor = "of")
public class FxRate1Input {
  FxRateId key;
  LocalDate date;
  FxRateType type;
  Rate rate;
}
