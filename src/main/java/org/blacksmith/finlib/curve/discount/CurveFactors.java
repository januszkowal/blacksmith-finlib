package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

public interface CurveFactors {
  double interestRate(LocalDate date);

  double interestRate(double yearFraction);

  double yearFraction(LocalDate date);

  double discountFactor(LocalDate date);

  double forwardRate(LocalDate d1, LocalDate d2);
}
