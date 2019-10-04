package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Set;

public class YearMonthDaySetPolicy extends AbstractHolidaySetPolicy<LocalDate> {

  public YearMonthDaySetPolicy() {  }

  public YearMonthDaySetPolicy(Set<LocalDate> holidays) {
    this.addAll(holidays);
  }

  public LocalDate convertDate(LocalDate date) {
    return date;
  }
}
