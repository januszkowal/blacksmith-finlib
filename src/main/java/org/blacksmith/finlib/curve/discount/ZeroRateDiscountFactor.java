package org.blacksmith.finlib.curve.discount;

/**
 * Zero-coupon continuously-compounded rates.
 * PV = FV / (e^(r*t)) = FV * DCF
 * FV = PV * e^(r*t) = PV / DCF
 * DCF = 1 / (e^(r*t)) = e^(-rate*t)
 */
public class ZeroRateDiscountFactor implements DiscountFactor {
  public static ZeroRateDiscountFactor of() {
    return new ZeroRateDiscountFactor();
  }
  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.exp(-interestRate * yearFraction);
  }
}
