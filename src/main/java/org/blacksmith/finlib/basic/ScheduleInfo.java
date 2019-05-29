package org.blacksmith.finlib.basic;

import java.time.LocalDate;

public class ScheduleInfo {
  private final boolean isEndOfMonthConvention;

  public ScheduleInfo(boolean isEndOfMonthConvention) {
    this.isEndOfMonthConvention = isEndOfMonthConvention;
  }

  public static ScheduleInfo SIMPLE_SCHEDULE_INFO = new ScheduleInfo(false);

  public LocalDate getStartDate() {
    return null;
  }
  public LocalDate getEndDate() {
    return null;
  }
  public LocalDate getPeriodEndDate(LocalDate xdate) {
    return null;
  }
  public boolean isEndOfMonthConvention() {
    return this.isEndOfMonthConvention;
  }
  public Frequency getFrequency() {
    return null;
  }
}
