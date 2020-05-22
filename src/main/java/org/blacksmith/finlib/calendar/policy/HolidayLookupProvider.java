package org.blacksmith.finlib.calendar.policy;

public interface HolidayLookupProvider<U> {
  boolean contains(U key);
}
