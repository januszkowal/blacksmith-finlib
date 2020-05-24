package org.blacksmith.finlib.calendar.policy.lookup;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.finlib.calendar.policy.lookup.HolidayLookupProvider;

public class HolidayLookupMutableContainer<U> implements HolidayLookupProvider<U> {

  protected Set<U> holidays;

  public HolidayLookupMutableContainer() {
    this.holidays = new HashSet<>();
  }

  public HolidayLookupMutableContainer(Collection<U> holidays) {
    this.holidays = new HashSet<>(holidays);
  }

  @Override
  public boolean contains(U key) {
    return holidays.contains(key);
  }

  public void add(U holiday) {
    Validate.notNull(holiday, "Null holiday not allowed");
    this.holidays.add(holiday);
  }

  public void addAll(Collection<U> holidays) {
    Validate.notNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(holidays);
  }

  @SafeVarargs
  public final void addAll(U...holidays) {
    Validate.notNull(holidays, "Null holidays list not allowed");
    this.holidays.addAll(Arrays.stream(holidays).collect(Collectors.toSet()));
  }

  public void clear() {
    holidays.clear();
  }
}
