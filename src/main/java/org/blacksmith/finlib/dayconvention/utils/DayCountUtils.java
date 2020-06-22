package org.blacksmith.finlib.dayconvention.utils;

public class DayCountUtils {
  private DayCountUtils() {}
  public static int days360(int y1, int m1, int d1, int y2, int m2, int d2) {
    return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
  }

  public static int days360(YMD date1, YMD date2) {
    return days360(date1.getYear(), date1.getMonth(), date1.getDay(), date2.getYear(), date2.getMonth(), date2.getDay());
  }
}
