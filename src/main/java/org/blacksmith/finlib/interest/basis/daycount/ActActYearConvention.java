package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class ActActYearConvention extends AbstractConvention {

  public ActActYearConvention() {
    super(false);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate calcDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, calcDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    final long years = DateUtils.yearsBetween(firstDate, secondDate);
    final LocalDate remainedPeriodStartDate = years == 0 ? firstDate : firstDate.plusYears(years);
    final long remainedPeriodDays = DateUtils.daysBetween(remainedPeriodStartDate, secondDate);
    final double remainedPeriodYearLength = DateUtils.daysBetween(remainedPeriodStartDate, remainedPeriodStartDate.plusYears(1));
    return years + (remainedPeriodDays / remainedPeriodYearLength);
  }
}
