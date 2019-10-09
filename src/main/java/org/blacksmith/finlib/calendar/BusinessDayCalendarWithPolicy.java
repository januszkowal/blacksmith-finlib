package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import org.blacksmith.commons.arg.Validate;

public class BusinessDayCalendarWithPolicy implements BusinessDayCalendar {
  private final HolidayPolicy holidayPolicy;

  public BusinessDayCalendarWithPolicy(HolidayPolicy holidayPolicy) {
    Validate.notNull(holidayPolicy, "Null holiday policy not allowed");
    this.holidayPolicy = holidayPolicy;
  }

  public static BusinessDayCalendarWithPolicy of(HolidayPolicy holidayPolicy) {
    return new BusinessDayCalendarWithPolicy(holidayPolicy);
  }

  public boolean isHoliday(LocalDate date) {
    Validate.notNull(date);
    return holidayPolicy.isHoliday(date);
  }
}
