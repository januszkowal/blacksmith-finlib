package org.blacksmith.finlib.interestbasis.daycount;

import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.nextLeapDay;

import java.time.LocalDate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class ActAct365LConvention implements DayCountConventionCalculator {
  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    int actualDays = DateUtils.daysBetween(startDate, endDate);
    if (startDate.equals(endDate)) {
      return 0d;
    }
    // calculation is based on the end of the schedule period (next coupon date) and annual/non-annual frequency
    LocalDate couponEndDate = scheduleInfo.getCouponEndDate();
    if (scheduleInfo.getCouponFrequency().isAnnual()) {
      LocalDate nextLeap = DateUtils.nextLeapDay(startDate);
      return actualDays / (nextLeap.isAfter(couponEndDate) ? 365d : 366d);
    } else {
      return actualDays / (couponEndDate.isLeapYear() ? 366d : 365d);
    }
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate calcDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, calcDate);
  }

}