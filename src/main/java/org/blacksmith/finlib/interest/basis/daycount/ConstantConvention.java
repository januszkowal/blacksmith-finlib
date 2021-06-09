package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.ScheduleInfo;

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
