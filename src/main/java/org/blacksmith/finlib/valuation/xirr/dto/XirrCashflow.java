package org.blacksmith.finlib.valuation.xirr.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.valuation.xirr.Cashflow;

/**
 * Convenient class which represents {@link Cashflow} instances more
 * conveniently for calculating purposes (present and derivative)
 */
public final class XirrCashflow {
  /**
   * The amount of the cashflow.
   */
  private final double amount;
  /**
   * The number of years for which the cashflow applies, including
   * fractional years.
   */
  private final double years;

  public XirrCashflow(double amount, double years) {
    this.amount = amount;
    this.years = years;
  }

  public static XirrCashflow of(double amount, double years) {
    return new XirrCashflow(amount, years);
  }

  public static List<XirrCashflow> negate(List<XirrCashflow> cashflows) {
    return cashflows.stream().map(XirrCashflow::negate).collect(Collectors.toList());
  }

  /**
   * Future value of the cashflow at the given rate.
   *
   * @param rate the rate of return
   * @return present value of the investment at the given rate
   */
  public double futureValue(final double rate) {
    if (rate > -1.0) {
      return amount * Math.pow(1 + rate, years);
    } else if (rate < -1.0) {
      // Extend the function into the range where the rate is less
      // than -100%.  Even though this does not make practical sense,
      // it allows the algorithm to converge in the cases where the
      // candidate values enter this range

      // We cannot use the same formula as before, since the base of
      // the exponent (1+rate) is negative, this yields imaginary
      // values for fractional years.
      // E.g. if rate=-1.5 and years=.5, it would be (-.5)^.5,
      // i.e. the square root of negative one half.

      // Ensure the values are always negative so there can never
      // be a zero (as long as some amount is non-zero).
      // This formula also ensures that the derivative is positive
      // (when rate < -1) so that Newton's method is encouraged to
      // move the candidate values towards the proper range

      //return -Math.abs(amount) * Math.pow(-1 - rate, years);
      return -amount * Math.pow(-1 - rate, years);
    } else if (years == 0) {
      return amount; // Resolve 0^0 as 0
    } else {
      return 0;
    }
  }

  /**
   * Derivative of the present value of the investment at the given rate.
   *
   * @param rate the rate of return
   * @return derivative of the present value at the given rate
   */
  public double derivativeValue(final double rate) {
    if (years == 0) {
      return 0;
    } else if (rate > -1.0) {
      return amount * years * Math.pow(1 + rate, years - 1);
    } else if (rate < -1.0) {
      //return Math.abs(amount) * years * Math.pow(-1 - rate, years - 1);
      return -amount * years * Math.pow(-1 - rate, years - 1);
    } else {
      return 0;
    }
  }

  public XirrCashflow negate() {
    return new XirrCashflow(-this.amount, this.years);
  }
}
