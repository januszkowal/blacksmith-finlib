package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public interface DayCountConventionCalculator {
  default void verify(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    ArgChecker.inOrderOrEqual(startDate, endDate, () -> "StartDate and EndDate must be in time-line order");
    if (requireScheduleInfo()) {
      ArgChecker.notNull(scheduleInfo, () -> "Schedule info must be not null");
      ArgChecker
          .inOrderOrEqual(scheduleInfo.getStartDate(), startDate, () -> "PeriodStartDate and CouponStartDate must be in time-line order");
      ArgChecker.inOrderOrEqual(endDate, scheduleInfo.getEndDate(), () -> "PeriodEndDate and CouponEndDate must be in time-line order");
      ArgChecker.inOrderOrEqual(scheduleInfo.getCouponStartDate(), startDate, () -> "StartDate mustn't be before CouponStartDate");
      ArgChecker.inOrderOrEqual(endDate, scheduleInfo.getCouponEndDate(), () -> "EndDate mustn't be after CouponEndDate");
    }
  }

  default long days(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    verify(startDate, endDate, scheduleInfo);
    return calculateDays(startDate, endDate, scheduleInfo);
  }

  default double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    verify(startDate, endDate, scheduleInfo);
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateYearFraction(startDate, endDate, scheduleInfo);
  }

  default boolean requireScheduleInfo() {
    return false;
  }

  long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);

  double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);
}
