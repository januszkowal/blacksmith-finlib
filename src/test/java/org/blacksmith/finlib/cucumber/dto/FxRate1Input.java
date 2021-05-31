package org.blacksmith.finlib.cucumber.dto;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.fxrate.FxRateId;
import org.blacksmith.finlib.rate.fxrate.FxRateType;

import lombok.Value;

@Value(staticConstructor = "of")
public class FxRate1Input {
  FxRateId key;
  LocalDate date;
  FxRateType type;
  Rate rate;
}
