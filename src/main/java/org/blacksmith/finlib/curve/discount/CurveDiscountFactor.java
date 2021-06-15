package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

public interface CurveDiscountFactor {
  double interestRate(LocalDate date);

  double yearFraction(LocalDate date);

  double discountFactor(LocalDate date);

  double forwardRate(LocalDate d1, LocalDate d2);
}
