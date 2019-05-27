package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayProvider;

public class MonthDaySetHolidayProvider
    implements HolidayProvider<MonthDay>
{

  private final Set<MonthDay> holidays = new HashSet<>();

  public MonthDaySetHolidayProvider() { }

  public MonthDaySetHolidayProvider(Set<MonthDay> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays);
  }

  public void add(MonthDay d) {
    this.holidays.add(d);
  }

  public void add(Collection<MonthDay> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays); }

  public void clear() {
    holidays.clear();
  }

  @Override
  public boolean contains(LocalDate date) {
    return holidays.contains(convertDate(date));
  }

  private MonthDay convertDate(LocalDate date) {
    return MonthDay.of(date.getMonthValue(),date.getDayOfMonth());
  }
}
