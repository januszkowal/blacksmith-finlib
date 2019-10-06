package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;

public interface HolidayProvider {
  boolean isHoliday(LocalDate date);
}
