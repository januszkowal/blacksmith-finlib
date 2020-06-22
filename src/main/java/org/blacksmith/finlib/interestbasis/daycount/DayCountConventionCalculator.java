package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public interface DayCountConventionCalculator {
  default void verify(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Dates must be in time-line order");
    }
    if (scheduleInfo!=null) {
      if (startDate.isBefore(scheduleInfo.getStartDate())) {
        throw new IllegalArgumentException("Dates must be in time-line order");
      }
      if (endDate.isAfter(scheduleInfo.getEndDate())) {
        throw new IllegalArgumentException("Dates must be in time-line order");
      }
      if (startDate.isBefore(scheduleInfo.getCouponStartDate())) {
        throw new IllegalArgumentException("Start musn't be before coupon start");
      }
      if (endDate.isAfter(scheduleInfo.getCouponEndDate())) {
        throw new IllegalArgumentException("End musn't be after coupon end");
      }
    }
  }
  default int days(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    verify(startDate,endDate,scheduleInfo);
    return calculateDays(startDate, endDate, scheduleInfo);
  }

  default double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    verify(startDate,endDate,scheduleInfo);
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateYearFraction(startDate, endDate, scheduleInfo);
  }

  int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);
  double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);
}
