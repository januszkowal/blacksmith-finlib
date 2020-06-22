package org.blacksmith.finlib.interestbasis.daycount;

import org.blacksmith.commons.datetime.DateUtils;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class ActActAfbConvention implements DayCountConventionCalculator {
  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    final int years = DateUtils.yearsBetween(startDate, endDate);
    final LocalDate remainedPeriodEndDate = (years==0) ? endDate: endDate.minusYears(years);
    final int remainedPeriodDays = calculateDays(startDate, remainedPeriodEndDate, scheduleInfo);
    final double remainedPeriodYearLength = DateUtils.isLeapDayInPeriod(startDate,remainedPeriodEndDate) ? 366d : 365d;
    return years + (remainedPeriodDays / remainedPeriodYearLength);
  }
}
