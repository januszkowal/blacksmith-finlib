package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.impl.AbstractConstantDenominatorConvention;

public class NLConvention extends AbstractConstantDenominatorConvention {

  public NLConvention(double denominator) {
    super(denominator);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return DateUtils.daysBetween(firstDate, secondDate) - DateUtils.numberOfLeapDays(firstDate, secondDate);
  }
}
