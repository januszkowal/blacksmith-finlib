package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

public interface DiscountFactor {
  default double discountFactor(LocalDate date, double interestRate) {
    double yearFraction = relativeYearFraction(date);
    return discountFactor(yearFraction, interestRate);
  }

  double relativeYearFraction(LocalDate date);

  double discountFactor(double yearFraction, double interestRate);

//  double zeroRate();
//    double yearFractionMod = Math.max(EFFECTIVE_ZERO, yearFraction);
//    double discountFactor = discountFactor(yearFractionMod);
//    return -Math.log(discountFactor) / yearFractionMod;
//  }
}
