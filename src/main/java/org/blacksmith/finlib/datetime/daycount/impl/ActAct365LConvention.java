package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.impl.AbstractConvention;
import org.blacksmith.finlib.datetime.daycount.impl.DayCountConvention;

public class ActAct365LConvention extends AbstractConvention implements DayCountConvention {
  public ActAct365LConvention() {
    super(true);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, secondDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    long actualDays = DateUtils.daysBetween(firstDate, secondDate);
    if (firstDate.equals(secondDate)) {
      return 0d;
    }
    // calculation is based on the end of the schedule period (next coupon date) and annual/non-annual frequency
    LocalDate periodEndDate = scheduleInfo.getPeriodEndDate();
    if (scheduleInfo.getCouponFrequency().isAnnual()) {
      return actualDays / (DateUtils.isLeapDayInPeriod(firstDate, periodEndDate) ? 366d : 365d);
    } else {
      return actualDays / (periodEndDate.isLeapYear() ? 366d : 365d);
    }
  }
}
