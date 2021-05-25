package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class ActActYearConvention implements DayCountConventionCalculator {

  @Override
  public long calculateDays(LocalDate startDate, LocalDate calcDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, calcDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    final long years = DateUtils.yearsBetween(startDate, endDate);
    final LocalDate remainedPeriodStartDate = years == 0 ? startDate : startDate.plusYears(years);
    final long remainedPeriodDays = DateUtils.daysBetween(remainedPeriodStartDate, endDate);
    final double remainedPeriodYearLength = DateUtils.daysBetween(remainedPeriodStartDate, remainedPeriodStartDate.plusYears(1));
    return years + (remainedPeriodDays / remainedPeriodYearLength);
  }
}