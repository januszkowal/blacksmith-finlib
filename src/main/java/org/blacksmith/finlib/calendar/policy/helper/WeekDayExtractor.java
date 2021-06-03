package org.blacksmith.finlib.calendar.policy.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class WeekDayExtractor implements DatePartExtractor<DayOfWeek> {
  private final static WeekDayExtractor instance = new WeekDayExtractor();

  public static WeekDayExtractor getInstance() {
    return instance;
  }

  @Override
  public DayOfWeek extract(LocalDate date) {
    return date.getDayOfWeek();
  }
}
