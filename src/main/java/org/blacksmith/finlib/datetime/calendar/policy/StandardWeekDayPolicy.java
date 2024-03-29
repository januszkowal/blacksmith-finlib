package org.blacksmith.finlib.datetime.calendar.policy;

import java.time.DayOfWeek;
import java.time.LocalDate;

import org.blacksmith.finlib.datetime.calendar.HolidayPolicy;

public enum StandardWeekDayPolicy implements HolidayPolicy {
  SAT_SUN(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY),
  FRI_SAT(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY),
  THU_FRI(DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
  private final WeekDayPolicy weekDayPolicy;

  StandardWeekDayPolicy(DayOfWeek... weekendDays) {
    this.weekDayPolicy = WeekDayPolicy.of(weekendDays);
  }

  @Override
  public boolean isHoliday(LocalDate date) {
    return weekDayPolicy.isHoliday(date);
  }
}
