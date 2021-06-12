package org.blacksmith.finlib.curve.discount;

public interface DiscountFactor {
  double discountFactor(double yearFraction, double interestRate);
}
