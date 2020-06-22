package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class ActConvention extends AbstractSimpleConvention {

  public ActConvention(double denominator) {
    super(denominator);
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate);
  }
}
