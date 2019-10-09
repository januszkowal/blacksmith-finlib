package org.blacksmith.finlib.calendar;

import java.time.LocalDate;

public interface HolidayPolicy {
  boolean isHoliday(LocalDate date);
}
