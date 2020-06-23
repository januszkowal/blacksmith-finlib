package org.blacksmith.finlib.interestbasis.daycount;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;

public class D30UConvention implements DayCountConventionCalculator {

  private final DayCountConventionCalculator thirtyIsda;
  private final DayCountConventionCalculator thirtyUsEom;

  public D30UConvention(double denominator) {
    this.thirtyIsda = new D30IsdaConvention(denominator);
    this.thirtyUsEom = new D30USEomConvention(denominator);
  }

  @Override
  public boolean requireScheduleInfo() {
    return true;
  }

  @Override
  public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return thirtyIsda.calculateDays(startDate,endDate,scheduleInfo);
  }

  @Override
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    if (scheduleInfo.isEndOfMonthConvention()) {
      return thirtyUsEom.calculateYearFraction(startDate, endDate, scheduleInfo);
    } else {
      return thirtyIsda.calculateYearFraction(startDate, endDate, scheduleInfo);
    }
  }
}
