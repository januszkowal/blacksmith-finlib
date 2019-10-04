package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Set;

public class YearMonthDayPolicySet extends AbstractHolidayPolicySet<LocalDate> {

  public YearMonthDayPolicySet() {  }

  public YearMonthDayPolicySet(Set<LocalDate> holidays) {
    this.addAll(holidays);
  }

  public LocalDate convertDate(LocalDate date) {
    return date;
  }
}
