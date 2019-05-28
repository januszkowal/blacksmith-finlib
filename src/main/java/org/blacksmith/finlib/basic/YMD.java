package org.blacksmith.finlib.basic;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
