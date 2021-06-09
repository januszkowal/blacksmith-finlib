package org.blacksmith.finlib.pricer;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.DayCount;

/**
 * Zero-coupon continuously-compounded rates.
 */
public class ZeroRatePeriodicDiscountFactor implements DiscountFactor {
  private final int frequency;
  private final DayCount dayCount;
  private final LocalDate valuationDate;

  public ZeroRatePeriodicDiscountFactor(LocalDate valuationDate, DayCount dayCount, int frequency) {
    this.valuationDate = valuationDate;
    this.dayCount = dayCount;
    this.frequency = frequency;
  }

  @Override
  public double relativeYearFraction(LocalDate date) {
    return dayCount.relativeYearFraction(valuationDate, date);
  }

  /** convert zero rate periodically compounded to discount factor */
  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.pow(1d + interestRate / frequency, -yearFraction * frequency);
  }
}
