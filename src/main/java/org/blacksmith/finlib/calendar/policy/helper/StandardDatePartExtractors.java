package org.blacksmith.finlib.calendar.policy.helper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;

import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartExtractor;

public enum StandardDatePartExtractors implements DatePartExtractor {
  WEEK_DAY() {
    @Override
    public DayOfWeek extract(LocalDate value) {
      return value.getDayOfWeek();
    }
  },
  MONTH_DAY() {
    @Override
    public MonthDay extract(LocalDate value) {
      return MonthDay.from(value);
    }
  },
  DATE() {
    @Override
    public LocalDate extract(LocalDate value) {
      return value;
    }
  }
}
