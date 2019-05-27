package org.blacksmith.finlib.basic;

import java.time.LocalDate;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;

public interface BusinessDayConvention {
  LocalDate adjust(LocalDate date, BusinessDayCalendar calendar);
}
