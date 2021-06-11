package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class Act365ActConvention extends AbstractConvention {

  public Act365ActConvention() {
    super(false);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, secondDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    long actualDays = DateUtils.daysBetween(firstDate, secondDate);
    double denominator = DateUtils.isLeapDayInPeriod(firstDate, secondDate) ? 366d : 365d;
    return actualDays / denominator;
  }
}
