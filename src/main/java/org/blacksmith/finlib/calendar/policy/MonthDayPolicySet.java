package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

public class MonthDayPolicySet extends AbstractHolidayPolicySet<MonthDay>
{
  public MonthDayPolicySet() {
    super();
  }

  public MonthDayPolicySet(Set<MonthDay> holidays) {
    super(holidays);
  }

  public MonthDay convertDate(LocalDate date) {
    return MonthDay.of(date.getMonthValue(),date.getDayOfMonth());
  }
}
