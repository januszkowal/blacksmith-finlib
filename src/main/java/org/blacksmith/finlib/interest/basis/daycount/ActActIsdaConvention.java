package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class ActActIsdaConvention extends AbstractConvention {

  public ActActIsdaConvention() {
    super(false);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate calcDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, calcDate);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    int y1 = firstDate.getYear();
    int y2 = secondDate.getYear();
    int firstYearLength = firstDate.lengthOfYear();
    if (y1 == y2) {
      int actualDays = secondDate.getDayOfYear() - firstDate.getDayOfYear();
      return (double) actualDays / firstYearLength;
    } else {
      int firstRemainderOfYear = firstYearLength - firstDate.getDayOfYear() + 1;
      int secondRemainderOfYear = secondDate.getDayOfYear() - 1;
      int secondYearLength = secondDate.lengthOfYear();
      return (double) firstRemainderOfYear / firstYearLength +
          (double) secondRemainderOfYear / secondYearLength +
          (y2 - y1 - 1);
    }
  }
}
