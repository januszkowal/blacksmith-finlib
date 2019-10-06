package org.blacksmith.finlib.calendar.policy.helper;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

public interface DateToPartConverter<U extends TemporalAccessor> {
  U convert(LocalDate date);
}
