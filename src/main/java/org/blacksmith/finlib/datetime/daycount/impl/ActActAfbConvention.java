package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.impl.AbstractConvention;

public class ActActAfbConvention extends AbstractConvention {

  public ActActAfbConvention() {
    super(false);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, secondDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    final long years = DateUtils.yearsBetween(firstDate, secondDate);
    final LocalDate remainedPeriodEndDate = (years == 0) ? secondDate : secondDate.minusYears(years);
    final long remainedPeriodDays = DateUtils.daysBetween(firstDate, remainedPeriodEndDate);
    final double remainedPeriodYearLength = DateUtils.isLeapDayInPeriodExcl(firstDate, remainedPeriodEndDate) ? 366d : 365d;
    return years + (remainedPeriodDays / remainedPeriodYearLength);
  }
}
