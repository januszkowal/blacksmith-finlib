package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class ActActAfbConvention implements DayCountConventionCalculator {

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    final long years = DateUtils.yearsBetween(startDate, endDate);
    final LocalDate remainedPeriodEndDate = (years == 0) ? endDate : endDate.minusYears(years);
    final long remainedPeriodDays = calculateDays(startDate, remainedPeriodEndDate, scheduleInfo);
    final double remainedPeriodYearLength = DateUtils.isLeapDayInPeriod(startDate, remainedPeriodEndDate) ? 366d : 365d;
    return years + (remainedPeriodDays / remainedPeriodYearLength);
  }
}
