package org.blacksmith.finlib.datetime.daycount.impl;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;

public interface DayCountConvention {
  double yearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);
  long days(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);
}
