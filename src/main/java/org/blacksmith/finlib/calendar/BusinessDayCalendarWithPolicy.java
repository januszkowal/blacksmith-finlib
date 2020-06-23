package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import org.blacksmith.commons.arg.ArgChecker;

public class BusinessDayCalendarWithPolicy implements BusinessDayCalendar {
  private final HolidayPolicy holidayPolicy;

  public BusinessDayCalendarWithPolicy(HolidayPolicy holidayPolicy) {
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
