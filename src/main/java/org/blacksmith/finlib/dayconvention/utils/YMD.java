package org.blacksmith.finlib.dayconvention.utils;

import java.time.LocalDate;

@SuppressWarnings("unused")
public class YMD {
  int year;
  int month;
  int day;

  public YMD(final int year, final int month, final int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public static YMD of(LocalDate date) {
    return new YMD(date.getYear(),date.getMonthValue(),date.getDayOfMonth());
  }

  public static YMD of(final int year, final int month, final int day) {
    return new YMD(year,month,day);
  }

  public int getYear() { return this.year;}
  public void setYear(final int year) { this.year = year;}
  public int getMonth() { return this.month;}
  public void setMonth(final int month) { this.month = month;}
  public int getDay() { return this.day;}
  public void setDay(final int day) { this.day = day;}

  public void setFirstDayOfNextMonth() {
    this.month++;
    this.day=1;
  }

  public LocalDate asDate() {
    return LocalDate.of(this.year,this.month,this.day);
  }
}
