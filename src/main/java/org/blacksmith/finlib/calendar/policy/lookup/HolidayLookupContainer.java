package org.blacksmith.finlib.calendar.policy.lookup;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HolidayLookupContainer<U> implements HolidayLookupProvider<U> {

  protected final Set<U> holidays;

  public HolidayLookupContainer(Collection<U> holidays) {
    this.holidays = new HashSet<>(holidays);
  }

  public static <U> HolidayLookupContainer<U> of(Collection<U> holidays) {
    return new HolidayLookupContainer<>(holidays);
  }

  @SafeVarargs
  public static <U> HolidayLookupContainer<U> of(U... holidays) {
    return new HolidayLookupContainer<>(Set.of(holidays));
  }

  @Override
  public boolean contains(U key) {
    return holidays.contains(key);
  }
}
