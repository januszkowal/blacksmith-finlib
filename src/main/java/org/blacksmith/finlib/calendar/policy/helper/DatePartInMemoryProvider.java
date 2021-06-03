package org.blacksmith.finlib.calendar.policy.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class DatePartInMemoryProvider<U> implements DatePartProvider<U> {

  protected final Set<U> holidays;

  public DatePartInMemoryProvider(Collection<U> holidays) {
    this.holidays = new HashSet<>(holidays);
  }

  public static <U> DatePartInMemoryProvider<U> of(Collection<U> holidays) {
    return new DatePartInMemoryProvider<>(holidays);
  }

  @SafeVarargs
  public static <U> DatePartInMemoryProvider<U> of(U... holidays) {
    return new DatePartInMemoryProvider<>(Set.of(holidays));
  }

  @Override
  public boolean contains(U key) {
    return holidays.contains(key);
  }
}
