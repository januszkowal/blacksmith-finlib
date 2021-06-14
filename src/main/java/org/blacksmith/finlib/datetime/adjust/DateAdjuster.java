package org.blacksmith.finlib.datetime.adjust;

import java.time.LocalDate;

public interface DateAdjuster {
  LocalDate adjust(LocalDate date);
}
