package org.blacksmith.finlib.dayconvention;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.calendar.BusinessDayCalendar;

public interface BusinessDayConvention {
  LocalDate adjust(LocalDate date, BusinessDayCalendar calendar);
}
