package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DayCountBaseTest {
  public LocalDate date(String s) {
    return LocalDate.parse(s, DateTimeFormatter.BASIC_ISO_DATE);
  }
}
