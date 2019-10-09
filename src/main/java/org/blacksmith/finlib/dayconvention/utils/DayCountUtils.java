package org.blacksmith.finlib.dayconvention.utils;

import java.time.LocalDate;


import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.isLastDayOfFebruary;
import static org.blacksmith.commons.datetime.DateUtils.numberOfLeapDays;

public class DayCountUtils {
  private DayCountUtils() {}
  public static int days360(int y1, int m1, int d1, int y2, int m2, int d2) {
    return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
  }

  public static int days360(YMD date1, YMD date2) {
    return days360(date1.getYear(), date1.getMonth(), date1.getDay(), date2.getYear(), date2.getMonth(), date2.getDay());
  }

  public static int daysBetween30ISDA(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay() == 31)
      date1.setDay(30);
    if (date2.getDay() == 31 && date1.getDay() == 30) {
      date2.setDay(30);
    }
    return days360(date1, date2);
  }

  public static int daysBetween30E(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay()==31)
      date1.setDay(30);
    if (date2.getDay()==31) {
      date2.setDay(30);
    }
    return days360(date1, date2);
  }
  public static int daysBetween30EPlus(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay()==31)
      date1.setDay(30);
    if (date2.getDay()==31) {
      date2.setFirstDayOfNextMonth();
    }
    return days360(date1, date2);
  }
  public static int daysBetween30A(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay()==31)
      date1.setDay(30);
    if (date2.getDay()==31) {
      if (date1.getDay() < 30)
      {
        date2.setFirstDayOfNextMonth();
      }
      else {
        date2.setDay(30);
      }
    }
    return days360(date1, date2);
  }

  public static int daysBetween30PSA(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay()==31 || isLastDayOfFebruary(startDate))
      date1.setDay(30);
    if (date2.getDay()==31 && date1.getDay()==30) {
      date2.setDay(30);
    }
    return days360(date1, date2);
  }

  public static int days365(LocalDate startDate, LocalDate endDate) {
    return daysBetween(startDate, endDate) - numberOfLeapDays(startDate, endDate.minusDays(1));
  }

  public static int daysBetween30EISDA(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (date1.getDay() == 31 || isLastDayOfFebruary(startDate))
      date1.setDay(30);
    if (date2.getDay() == 31 && isLastDayOfFebruary(endDate)) {
      date2.setDay(30);
    }
    return days360(date1, date2);
  }

  public static int daysBetween30USEOM(LocalDate startDate, LocalDate endDate) {
    YMD date1 = YMD.of(startDate);
    YMD date2 = YMD.of(endDate);
    if (isLastDayOfFebruary(startDate)) {
      if (isLastDayOfFebruary(endDate)) {
        date2.setDay(30);
      }
      date1.setDay(30);
    }
    if (date1.getDay() == 31) {
      date1.setDay(30);
    }
    if (date2.getDay() == 31 && date1.getDay() == 30) {
      date2.setDay(30);
    }
    return days360(date1, date2);
  }
}
