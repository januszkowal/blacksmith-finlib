package org.blacksmith.finlib.interest.basis.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.ScheduleInfo;

public interface DayCountConvention {
  double yearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);
  long days(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);
}
