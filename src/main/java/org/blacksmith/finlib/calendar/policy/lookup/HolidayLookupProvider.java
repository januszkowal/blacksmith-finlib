package org.blacksmith.finlib.calendar.policy.lookup;

public interface HolidayLookupProvider<U> {
  boolean contains(U key);
}
