package org.blacksmith.finlib.calendar.helper;

import java.time.LocalDate;

public interface DateToPartConverter<U> {
  U convert(LocalDate date);
}
