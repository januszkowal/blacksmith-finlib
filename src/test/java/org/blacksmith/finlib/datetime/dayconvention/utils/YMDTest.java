package org.blacksmith.finlib.datetime.dayconvention.utils;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class YMDTest {

  @Test
  void create() {
    LocalDate d = LocalDate.parse("2012-05-13");
    YmdDate v = YmdDate.of(d);
    assertEquals(2012, v.getYear());
    assertEquals(5, v.getMonth());
    assertEquals(13, v.getDay());
    assertEquals(d, v.asDate());
    d = LocalDate.parse("2016-08-17");
    v = YmdDate.of(d.getYear(), d.getMonthValue(), d.getDayOfMonth());
    assertEquals(2016, v.getYear());
    assertEquals(8, v.getMonth());
    assertEquals(17, v.getDay());
    assertEquals(d, v.asDate());
  }

  @Test
  void firstDayOfNextMonth() {
    LocalDate d = LocalDate.parse("2012-05-13");
    YmdDate v = YmdDate.of(d);
    v.setFirstDayOfNextMonth();
    assertEquals(2012, v.getYear());
    assertEquals(6, v.getMonth());
    assertEquals(1, v.getDay());
    d = LocalDate.parse("2012-12-17");
    v = YmdDate.of(d);
    v.setFirstDayOfNextMonth();
    assertEquals(2013, v.getYear());
    assertEquals(1, v.getMonth());
    assertEquals(1, v.getDay());
  }

  @Test
  void getYear() {
    YmdDate v = YmdDate.of(LocalDate.parse("2012-05-13"));
    v.setYear(2016);
    v.setMonth(8);
    v.setDay(17);
    assertEquals(2016, v.getYear());
    assertEquals(8, v.getMonth());
    assertEquals(17, v.getDay());
  }
}
