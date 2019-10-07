package org.blacksmith.finlib.calendar.policy.helper;

import java.time.LocalDate;

public interface DateToPartConverter<U> {
  U convert(LocalDate date);
}
