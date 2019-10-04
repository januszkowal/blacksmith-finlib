package org.blacksmith.finlib.calendar.policy;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.HolidayPolicy;
import org.blacksmith.finlib.calendar.HolidayProvider;

public abstract class AbstractHolidaySetPolicy<U extends TemporalAccessor> implements HolidayPolicy {

  private final Set<U> holidays = new HashSet<>();

  public AbstractHolidaySetPolicy() {}

  public AbstractHolidaySetPolicy(Set<U> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays);
  }

  public void add(U d) {
    this.holidays.add(d);
  }

  public void addAll(Collection<U> holidays) {
    Validate.checkNotNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays); }

  public void clear() {
    holidays.clear();
  }

  public abstract U convertDate(LocalDate date);

  @Override
  public boolean isHoliday(LocalDate date) {
    return holidays.contains(convertDate(date));
  }
}
