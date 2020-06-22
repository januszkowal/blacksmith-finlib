package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class NLConvention extends AbstractSimpleConvention {

  public NLConvention(double denominator) {
    super(denominator);
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate) - DateUtils.numberOfLeapDays(startDate, endDate.minusDays(1));
  }
}
