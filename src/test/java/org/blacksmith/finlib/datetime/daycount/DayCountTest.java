package org.blacksmith.finlib.datetime.daycount;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.datetime.daycount.ScheduleInfo;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.blacksmith.finlib.datetime.daycount.impl.ActActIcmaConvention;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DayCountTest {

  @Test
  public void testDayCountConvention() {
    DayCount dayCountConvention = StandardDayCounts.D30_360_ISDA;
    assertEquals(30, dayCountConvention.days(LocalDate.parse("2020-03-01"), LocalDate.parse("2020-04-01"), null));
    assertEquals(0.08333333333333333,
        dayCountConvention.relativeYearFraction(LocalDate.parse("2020-03-01"), LocalDate.parse("2020-04-01"), null));

    dayCountConvention = StandardDayCounts.ACT_365_25;
    assertEquals(31, dayCountConvention.days(LocalDate.parse("2020-03-01"), LocalDate.parse("2020-04-01"), null));
    assertEquals(0.08487337440109514,
        dayCountConvention.relativeYearFraction(LocalDate.parse("2020-03-01"), LocalDate.parse("2020-04-01"), null));
  }

  @Test
  public void testIcma1() {
    ActActIcmaConvention convention = new ActActIcmaConvention();
    LocalDate startDate = LocalDate.parse("2002-12-31");
    LocalDate endDate = LocalDate.parse("2003-06-30");
    ScheduleInfo scheduleInfo = ScheduleInfo.builder()
        .startDate(startDate)
        .endDate(endDate)
        .isEndOfMonthConvention(false)
        .periodStartDate(startDate)
        .periodEndDate(endDate)
        .couponFrequency(Frequency.P6M)
        .build();
    double result = convention.calculateYearFraction(startDate, endDate, scheduleInfo);
    assertEquals(0.5, result, 0.0);
  }

  @Test
  public void testIcma2Eom() {
    ActActIcmaConvention convention = new ActActIcmaConvention();
    LocalDate startDate = LocalDate.parse("2002-06-30");
    LocalDate endDate = LocalDate.parse("2002-12-31");
    ScheduleInfo scheduleInfo = ScheduleInfo.builder()
        .startDate(startDate)
        .endDate(endDate)
        .isEndOfMonthConvention(true)
        .periodStartDate(startDate)
        .periodEndDate(endDate)
        .couponFrequency(Frequency.P6M)
        .build();
    double result = convention.calculateYearFraction(startDate, endDate, scheduleInfo);
    assertEquals(0.5, result, 0.0);
  }

  @Test
  public void testIcma2() {
    ActActIcmaConvention convention = new ActActIcmaConvention();
    LocalDate startDate = LocalDate.parse("2002-06-30");
    LocalDate endDate = LocalDate.parse("2002-12-31");
    ScheduleInfo scheduleInfo = ScheduleInfo.builder()
        .startDate(startDate)
        .endDate(endDate)
        .isEndOfMonthConvention(false)
        .periodStartDate(startDate)
        .periodEndDate(endDate)
        .couponFrequency(Frequency.P6M)
        .build();
    double result = convention.calculateYearFraction(startDate, endDate, scheduleInfo);
    assertEquals(183 / 366d + 1 / 364d, result, 0.0);
  }

}
