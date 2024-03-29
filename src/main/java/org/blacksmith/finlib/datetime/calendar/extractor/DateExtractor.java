package org.blacksmith.finlib.datetime.calendar.extractor;

import java.time.LocalDate;

public class DateExtractor implements DatePartExtractor<LocalDate> {
  private final static DateExtractor instance = new DateExtractor();

  public static DateExtractor getInstance() {
    return instance;
  }

  @Override
  public LocalDate extract(LocalDate date) {
    return date;
  }
}
