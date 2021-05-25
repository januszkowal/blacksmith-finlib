package org.blacksmith.finlib.calendar.policy.helper;

import java.time.LocalDate;

public interface DatePartExtractor<U> {
  U extract(LocalDate date);
}
