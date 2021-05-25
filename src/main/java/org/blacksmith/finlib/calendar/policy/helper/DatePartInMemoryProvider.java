package org.blacksmith.finlib.calendar.policy.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartProvider;

public class DatePartInMemoryProvider<U> implements DatePartProvider<U> {

  protected final Set<U> holidays;

  public DatePartInMemoryProvider(Collection<U> holidays) {
    this.holidays = new HashSet<>(holidays);
  }

  public static <U> org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider<U> of(Collection<U> holidays) {
    return new org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider<>(holidays);
  }

  @SafeVarargs
  public static <U> org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider<U> of(U... holidays) {
    return new org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider<>(Set.of(holidays));
  }

  @Override
  public boolean contains(U key) {
    return holidays.contains(key);
  }
}
