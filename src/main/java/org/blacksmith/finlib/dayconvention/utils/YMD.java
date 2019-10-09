package org.blacksmith.finlib.dayconvention.utils;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YMD {
  int year;
  int month;
  int day;

  public static YMD of(LocalDate date) {
    return new YMD(date.getYear(),date.getMonthValue(),date.getDayOfMonth());
  }
  public static YMD of(int year, int month, int day) {
    return new YMD(year,month,day);
  }

  public void setFirstDayOfNextMonth() {
    this.month++;
    this.day=1;
  }
}
