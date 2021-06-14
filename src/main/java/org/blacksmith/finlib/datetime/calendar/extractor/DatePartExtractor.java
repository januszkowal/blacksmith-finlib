package org.blacksmith.finlib.datetime.calendar.extractor;

import java.time.LocalDate;

public interface DatePartExtractor<U> {
  U extract(LocalDate date);
}
