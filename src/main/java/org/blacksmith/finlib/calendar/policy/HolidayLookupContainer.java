package org.blacksmith.finlib.calendar.policy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HolidayLookupContainer<U> implements HolidayLookupProvider<U> {
  protected Set<U> holidays;

  public HolidayLookupContainer(Collection<U> holidays) {
    this.holidays = new HashSet<>(holidays);
  }

  public static <U> HolidayLookupContainer of (Collection<U> holidays) {
    return new HolidayLookupContainer(holidays);
  }

  public static <U> HolidayLookupContainer of (U ... holidays) {
    return new HolidayLookupContainer(Set.of(holidays));
  }

  @Override
  public boolean contains(U key) {
    return holidays.contains(key);
  }
}
