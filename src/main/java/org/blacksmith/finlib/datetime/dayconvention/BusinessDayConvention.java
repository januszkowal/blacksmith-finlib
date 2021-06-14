package org.blacksmith.finlib.datetime.dayconvention;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.calendar.BusinessDayCalendar;

public interface BusinessDayConvention {
  LocalDate adjust(LocalDate date, BusinessDayCalendar calendar);
}
