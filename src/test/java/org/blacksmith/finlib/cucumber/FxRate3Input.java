package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRateId;

import lombok.Value;

@Value(staticConstructor = "of")
public class FxRate3Input {
  FxRateId key;
  LocalDate date;
  Rate buy;
  Rate sell;
  Rate avg;
}
