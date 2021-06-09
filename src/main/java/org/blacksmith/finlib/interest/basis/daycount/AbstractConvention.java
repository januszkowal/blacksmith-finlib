package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public abstract class AbstractConvention implements DayCountConvention {
  protected final boolean requireScheduleInfo;

  public AbstractConvention(boolean requireScheduleInfo) {
    this.requireScheduleInfo = requireScheduleInfo;
  }

  @Override
  public long days(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    verify(firstDate, secondDate, scheduleInfo);
    return calculateDays(firstDate, secondDate, scheduleInfo);
  }

  @Override
  public double yearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    verify(firstDate, secondDate, scheduleInfo);
    if (secondDate.isEqual(firstDate)) {
      return 0;
    }
    return calculateYearFraction(firstDate, secondDate, scheduleInfo);
  }

  public abstract long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);

  public abstract double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);

  public void verify(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    ArgChecker.inOrderOrEqual(firstDate, secondDate, () -> "FirstDate and SecondDate must be in time-line order");
    if (this.requireScheduleInfo) {
      ArgChecker.notNull(scheduleInfo, () -> "Schedule info must be not null");
      ArgChecker
          .inOrderOrEqual(scheduleInfo.getStartDate(), firstDate, () -> "PeriodStartDate and CouponStartDate must be in time-line order");
      ArgChecker.inOrderOrEqual(secondDate, scheduleInfo.getEndDate(), () -> "PeriodEndDate and CouponEndDate must be in time-line order");
      ArgChecker.inOrderOrEqual(scheduleInfo.getPeriodStartDate(), firstDate, () -> "FirstDate mustn't be before CouponStartDate");
      ArgChecker.inOrderOrEqual(secondDate, scheduleInfo.getPeriodEndDate(), () -> "SecondDate mustn't be after CouponEndDate");
    }
  }
}
