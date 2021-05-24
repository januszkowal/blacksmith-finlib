package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public abstract class AbstractConstantDenominatorConvention implements DayCountConventionCalculator {

  protected final double denominator;

  public AbstractConstantDenominatorConvention(double denominator) {
    this.denominator = denominator;
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    long actualDays = calculateDays(startDate, endDate, scheduleInfo);
    return actualDays / denominator;
  }
}
