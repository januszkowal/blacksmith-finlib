package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;

/**
 * Convenience class which represents {@link Cashflow} instances more
 * conveniently for calculating purposes
 */
public final class XirrCashflow {
  private LocalDate date;
  /** The amount of the cashflow. */
  private double amount;
  /** The number of years for which the cashflow applies, including
   * fractional years. */
  private double years;


  public XirrCashflow(LocalDate date, double amount, double years) {
    this.amount = amount;
    this.date = date;
    this.years = years;
  }

  public static XirrCashflow of(LocalDate date, double amount, double years) {
    return new XirrCashflow(date,amount,years);
  }
  /**
   * Future value of the cashflow at the given rate.
   * @param rate the rate of return
   * @return present value of the investment at the given rate
   */
  public double presentValue(final double rate) {
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

      return -Math.abs(amount) * Math.pow(-1 - rate, years);
    } else if (years == 0) {
      return amount; // Resolve 0^0 as 0
    } else {
      return 0;
    }
  }

  /**
   * Derivative of the present value of the investment at the given rate.
   * @param rate the rate of return
   * @return derivative of the present value at the given rate
   */
  public double derivative(final double rate) {
    if (years == 0) {
      return 0;
    } else if (-1 < rate) {
      return amount * years * Math.pow(1 + rate, years - 1);
    } else if (rate < -1) {
      return Math.abs(amount) * years * Math.pow(-1 - rate, years - 1);
    } else {
      return 0;
    }
  }

  public LocalDate getDate() { return this.date;}
  public double getAmount() { return  this.amount;}
}
