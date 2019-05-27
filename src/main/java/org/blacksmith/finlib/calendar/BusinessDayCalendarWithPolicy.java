package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import org.blacksmith.commons.arg.Validate;

public class BusinessDayCalendarWithPolicy implements BusinessDayCalendar {
  private final HolidayPolicy holidayPolicy;

  public BusinessDayCalendarWithPolicy(HolidayPolicy holidayPolicy) {
    Validate.checkNotNull(holidayPolicy, "Null holiday policy not allowed");
    this.holidayPolicy = holidayPolicy;
  }

  public boolean isHoliday(LocalDate date) {
    return holidayPolicy.isHoliday(date);
  }
}
