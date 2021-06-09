package org.blacksmith.finlib.pricer;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.DayCount;

/**
 * Zero-coupon continuously-compounded rates.
 * PV = FV / (e^(r*t)) = FV * DCF
 * FV = PV * e^(r*t) = PV / DCF
 * DCF = 1 / (e^(r*t)) = e^(-rate*t)
 */
public class ZeroRateDiscountFactor implements DiscountFactor {
  private final LocalDate valuationDate;
  private final DayCount dayCount;

  public ZeroRateDiscountFactor(LocalDate valuationDate, DayCount dayCount) {
    this.valuationDate = valuationDate;
    this.dayCount = dayCount;
  }

  @Override
  public double relativeYearFraction(LocalDate date) {
    return dayCount.relativeYearFraction(valuationDate, date);
  }

  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.exp(-interestRate * yearFraction);
  }
}
