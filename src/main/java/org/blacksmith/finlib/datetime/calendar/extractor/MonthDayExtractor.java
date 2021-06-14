package org.blacksmith.finlib.datetime.calendar.extractor;

import java.time.LocalDate;
import java.time.MonthDay;

public class MonthDayExtractor implements DatePartExtractor<MonthDay> {
  private final static MonthDayExtractor instance = new MonthDayExtractor();

  public static MonthDayExtractor getInstance() {
    return instance;
  }

  @Override
  public MonthDay extract(LocalDate date) {
    return MonthDay.from(date);
  }
}
