package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayProvider;

public class YearMonthDaySetHolidayProvider
    implements HolidayProvider<LocalDate> {

  private final Set<LocalDate> holidays = new HashSet<>();

  public YearMonthDaySetHolidayProvider() {  }

  public YearMonthDaySetHolidayProvider(Set<LocalDate> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays);
  }

  public void add(LocalDate d) {
    this.holidays.add(d);
  }

  public void add(Collection<LocalDate> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays); }

  public void clear() {
    this.holidays.clear();
  }

  @Override
  public boolean contains(LocalDate date) {
    return holidays.contains(convertDate(date));
  }

  private LocalDate convertDate(LocalDate date) {
    return date;
  }
}
