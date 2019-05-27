package org.blacksmith.finlib.basic;

import java.time.LocalDate;

public interface DateOperation {
  LocalDate plus(LocalDate date, int amount);
  LocalDate minus(LocalDate date, int amount);
}
