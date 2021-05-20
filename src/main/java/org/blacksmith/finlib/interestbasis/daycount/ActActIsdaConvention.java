package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class ActActIsdaConvention implements DayCountConventionCalculator {

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    int y1 = startDate.getYear();
    int y2 = endDate.getYear();
    int firstYearLength = startDate.lengthOfYear();
    if (y1 == y2) {
      int actualDays = endDate.getDayOfYear() - startDate.getDayOfYear();
      return (double) actualDays / firstYearLength;
    } else {
      int firstRemainderOfYear = firstYearLength - startDate.getDayOfYear() + 1;
      int secondRemainderOfYear = endDate.getDayOfYear() - 1;
      int secondYearLength = endDate.lengthOfYear();
      return (double) firstRemainderOfYear / firstYearLength +
          (double) secondRemainderOfYear / secondYearLength +
          (y2 - y1 - 1);
    }
  }
}
