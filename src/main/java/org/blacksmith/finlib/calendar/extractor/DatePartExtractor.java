package org.blacksmith.finlib.calendar.extractor;

import java.time.LocalDate;

public interface DatePartExtractor<U> {
  U extract(LocalDate date);
}
