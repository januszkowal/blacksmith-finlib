package org.blacksmith.finlib.interestbasis.daycount;

import static org.blacksmith.commons.datetime.DateUtils.daysBetween;

import java.time.LocalDate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class Act365ActConvention implements DayCountConventionCalculator {

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    int actualDays = calculateDays(startDate, endDate,scheduleInfo);
    double denominator = DateUtils.isLeapDayInPeriod(startDate,endDate) ? 366d : 365d;
    return actualDays / denominator;
  }
}
