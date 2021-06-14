package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;

public class D30UConvention extends AbstractConvention {

  private final D30IsdaConvention thirtyIsda;
  private final D30USEomConvention thirtyUsEom;

  public D30UConvention(double denominator) {
    super(true);
    this.thirtyIsda = new D30IsdaConvention(denominator);
    this.thirtyUsEom = new D30USEomConvention(denominator);
  }

  @Override
  public long calculateDays(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    return thirtyIsda.calculateDays(firstDate, secondDate, scheduleInfo);
  }

  @Override
  public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    if (scheduleInfo.isEndOfMonthConvention()) {
      return thirtyUsEom.calculateYearFraction(firstDate, secondDate, scheduleInfo);
    } else {
      return thirtyIsda.calculateYearFraction(firstDate, secondDate, scheduleInfo);
    }
  }
}
