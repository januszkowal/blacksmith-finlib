package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;

public abstract class AbstractConstantDenominatorConvention extends AbstractConvention implements DayCountConvention {

  protected final double denominator;

  public AbstractConstantDenominatorConvention(boolean requireScheduleInfo, double denominator) {
    super(requireScheduleInfo);
    this.denominator = denominator;
  }

  public AbstractConstantDenominatorConvention(double denominator) {
    super(false);
    this.denominator = denominator;
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    long actualDays = calculateDays(firstDate, secondDate, scheduleInfo);
    return actualDays / denominator;
  }
}
