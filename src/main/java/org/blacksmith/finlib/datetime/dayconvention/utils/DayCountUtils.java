package org.blacksmith.finlib.datetime.dayconvention.utils;

import java.time.LocalDate;

public class DayCountUtils {
  private DayCountUtils() {
  }

  public static long days360(int y1, int m1, int d1, int y2, int m2, int d2) {
    return 360L * (y2 - y1) + 30L * (m2 - m1) + (d2 - d1);
  }

  public static long days360(YmdDate date1, YmdDate date2) {
    return days360(date1.getYear(), date1.getMonth(), date1.getDay(), date2.getYear(), date2.getMonth(), date2.getDay());
  }

  public static long days360(LocalDate date1, LocalDate date2) {
    return days360(date1.getYear(), date1.getMonthValue(), date1.getDayOfMonth(),
        date2.getYear(), date2.getMonthValue(), date2.getDayOfMonth());
  }

  public static long months360(LocalDate date1, LocalDate date2) {
    return 12L * (date2.getYear() - date1.getYear()) + (date2.getMonthValue() - date1.getMonthValue());
  }
}
