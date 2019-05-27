package org.blacksmith.finlib.calendar;

import java.time.LocalDate;

public interface HolidayProvider<T> {
  boolean contains(LocalDate date);
}
