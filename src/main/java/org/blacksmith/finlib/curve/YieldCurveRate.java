package org.blacksmith.finlib.curve;

import java.time.LocalDate;

import lombok.Value;

@Value(staticConstructor = "of")
public class YieldCurveRate {
  LocalDate date;
  boolean isKnot;
  double interestRate;
  double dcf;
}
