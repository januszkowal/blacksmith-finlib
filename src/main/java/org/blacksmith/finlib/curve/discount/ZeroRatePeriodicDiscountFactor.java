package org.blacksmith.finlib.curve.discount;

/**
 * Zero-coupon continuously-compounded rates.
 */
public class ZeroRatePeriodicDiscountFactor implements DiscountFactor {
  private final int frequency;

  public ZeroRatePeriodicDiscountFactor(int frequency) {
    this.frequency = frequency;
  }

  public static ZeroRatePeriodicDiscountFactor of(int frequency) {
    return new ZeroRatePeriodicDiscountFactor(frequency);
  }

  /** convert zero rate periodically compounded to discount factor */
  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.pow(1d + interestRate / frequency, -yearFraction * frequency);
  }
}
