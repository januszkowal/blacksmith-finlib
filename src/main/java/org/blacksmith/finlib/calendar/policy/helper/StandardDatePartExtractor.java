package org.blacksmith.finlib.calendar.policy.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;

public enum StandardDatePartExtractor implements DatePartExtractor {
  WEEK_DAY1() {
    @Override
    public DayOfWeek extract(LocalDate value) {
      return value.getDayOfWeek();
    }
  },
  MONTH_DAY1() {
    @Override
    public MonthDay extract(LocalDate value) {
      return MonthDay.from(value);
    }
  },
  DATE1() {
    @Override
    public LocalDate extract(LocalDate value) {
      return value;
    }
  }
}
