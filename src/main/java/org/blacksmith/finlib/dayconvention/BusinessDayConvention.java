package org.blacksmith.finlib.dayconvention;

import java.time.LocalDate;
import org.blacksmith.finlib.calendar.BusinessDayCalendar;
import org.blacksmith.finlib.dayconvention.utils.Pair;

public interface BusinessDayConvention {
  LocalDate adjust(LocalDate date, BusinessDayCalendar calendar);
  Pair<LocalDate> adjustPair(LocalDate date, BusinessDayCalendar calendar);
}
