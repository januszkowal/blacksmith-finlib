package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class Act365ActConvention implements DayCountConventionCalculator {

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    long actualDays = calculateDays(startDate, endDate, scheduleInfo);
    double denominator = DateUtils.isLeapDayInPeriod(startDate, endDate) ? 366d : 365d;
    return actualDays / denominator;
  }
}
