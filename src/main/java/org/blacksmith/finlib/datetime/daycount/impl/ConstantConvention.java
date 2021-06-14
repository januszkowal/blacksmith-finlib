package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.impl.AbstractConvention;

public class ConstantConvention extends AbstractConvention {
  private final long days;
  private final double yearFraction;

  public ConstantConvention(long days, double yearFraction) {
    super(false);
    this.days = days;
    this.yearFraction = yearFraction;
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return this.days;
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return this.yearFraction;
  }
}
