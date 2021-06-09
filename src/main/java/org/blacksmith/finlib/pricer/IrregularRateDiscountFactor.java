package org.blacksmith.finlib.pricer;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.DayCount;

/**
 * XNPV - Irregular rates
 * PV = FV / ((1 + r)^((di - d0) / 365)) = FV * DCF
 * DCF = 1 / ((1 + r)^((di - d0) / 365)) = (1 + r)^(-(di - d0) / 365)
 */
public class IrregularRateDiscountFactor implements DiscountFactor {
  private final int frequency;
  private final DayCount dayCount;
  private final LocalDate valuationDate;

  public IrregularRateDiscountFactor(LocalDate valuationDate, DayCount dayCount, int frequency) {
    this.valuationDate = valuationDate;
    this.dayCount = dayCount;
    this.frequency = frequency;
  }

  @Override
  public double relativeYearFraction(LocalDate date) {
    return dayCount.relativeYearFraction(valuationDate, date);
  }

  @Override
  public double discountFactor(double yearFraction, double interestRate) {
    return Math.pow(1d + interestRate, -yearFraction);
  }
}
