package org.blacksmith.finlib.calendar;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.basic.calendar.HolidayPolicy;

public class BusinessDayCalendarWithPolicy implements BusinessDayCalendar {
  private final HolidayPolicy holidayPolicy;

  public BusinessDayCalendarWithPolicy(org.blacksmith.finlib.basic.calendar.HolidayPolicy holidayPolicy) {
    ArgChecker.notNull(holidayPolicy, "Null holiday policy not allowed");
    this.holidayPolicy = holidayPolicy;
  }

  public static BusinessDayCalendarWithPolicy of(HolidayPolicy holidayPolicy) {
    return new BusinessDayCalendarWithPolicy(holidayPolicy);
  }

  public boolean isHoliday(LocalDate date) {
    ArgChecker.notNull(date);
    return holidayPolicy.isHoliday(date);
  }
}
