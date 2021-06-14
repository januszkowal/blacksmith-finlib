package org.blacksmith.finlib.datetime;

import java.time.LocalDate;

public interface DateAdjuster {
  LocalDate adjust(LocalDate date);
}
