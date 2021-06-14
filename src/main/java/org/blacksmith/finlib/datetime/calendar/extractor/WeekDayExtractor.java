package org.blacksmith.finlib.datetime.calendar.extractor;

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
