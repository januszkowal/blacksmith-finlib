package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public class NLConvention extends AbstractConstantDenominatorConvention {

  public NLConvention(double denominator) {
    super(denominator);
  }

  @Override
  public long calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(startDate, endDate) - DateUtils.numberOfLeapDays(startDate, endDate);
  }
}
