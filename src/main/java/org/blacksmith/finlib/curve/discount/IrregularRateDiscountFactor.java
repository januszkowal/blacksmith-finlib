package org.blacksmith.finlib.curve.discount;

/**
 * XNPV - Irregular rates
 * PV = FV / ((1 + r)^((di - d0) / 365)) = FV * DCF
 * DCF = 1 / ((1 + r)^((di - d0) / 365)) = (1 + r)^(-(di - d0) / 365)
 */
public class IrregularRateDiscountFactor implements DiscountFactor {
  private final int frequency;

  public IrregularRateDiscountFactor(int frequency) {
    this.frequency = frequency;
  }

  public static IrregularRateDiscountFactor of(int frequency) {
    return new IrregularRateDiscountFactor(frequency);
  }

  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.pow(1d + interestRate, -yearFraction);
  }
}
