package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public abstract class AbstractSimpleConvention implements DayCountConventionCalculator {

  protected final double denominator;

  public AbstractSimpleConvention(double denominator) {
    this.denominator = denominator;
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    int actualDays = calculateDays(startDate, endDate, scheduleInfo);
    return actualDays / denominator;
  }
}
