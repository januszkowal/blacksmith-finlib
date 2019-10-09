package org.blacksmith.finlib.calendar;

import org.blacksmith.finlib.basic.Frequency;
import org.blacksmith.finlib.interestbasis.DayCountConvention;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.nextOrSameLeapDay;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DayCountTest {

  private static  final Logger LOGGER = LoggerFactory.getLogger(DayCountTest.class);

  private void printFactor(DayCountConvention basis, LocalDate startDate, LocalDate endDate) {
    System.out.println(basis.toString() + ":"+basis.yearFraction(startDate,endDate,
        ScheduleInfo.builder()
            .couponEndDate(endDate)
            .couponStartDate(startDate)
            .maturityDate(endDate)
            .startDate(startDate)
            .build()));
  }
  private DayCountConvention testr1() {
    return  new DayCountConvention() {
      @Override public double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
        LocalDate end = endDate;
        LocalDate start = endDate.minusYears(1);
        long yearsx = 0;
        while (!start.isBefore(startDate)) {
          yearsx++;
          end = start;
          start = endDate.minusYears(yearsx + 1);
        }
        // calculate the remaining fraction, including start, excluding end
        long actualDays = daysBetween(startDate, end);
        LocalDate nextLeap = nextOrSameLeapDay(startDate);
        return yearsx + (actualDays / (nextLeap.isBefore(end) ? 366d : 365d));
      }

      public int days(LocalDate firstDate, LocalDate secondDate) {
        return 0;
      }
    };
  }
  @Test
  void test1() {
    LocalDate startDate = LocalDate.now();
    //LocalDate startDate = LocalDate.of(2020,2,1);
    LocalDate endDate = startDate.plusDays(400);
    List<StandardDayCountConvention> basiss = Arrays.asList(StandardDayCountConvention.ONE_ONE,
        StandardDayCountConvention.ACT_360, StandardDayCountConvention.ACT_364, StandardDayCountConvention.ACT_365, StandardDayCountConvention.ACT_365_25,
        StandardDayCountConvention.ACT_ACT_ISDA, StandardDayCountConvention.ACT_365_ACT,
        StandardDayCountConvention.NL_360, StandardDayCountConvention.NL_365,
        StandardDayCountConvention.D30_360_ISDA,
        StandardDayCountConvention.D30_360_PSA,
        StandardDayCountConvention.D30_E_360, StandardDayCountConvention.D30_E_365,
        StandardDayCountConvention.D30_EPLUS_360, StandardDayCountConvention.D30_U_360_EOM,
        StandardDayCountConvention.ACT_ACT_YEAR, StandardDayCountConvention.ACT_ACT_AFB).stream().collect(Collectors.toList());
    basiss.forEach(b->printFactor(b,startDate,endDate));
    printFactor(testr1(),startDate, endDate);
    System.out.println(Arrays.stream(StandardDayCountConvention.values()).filter(b->!basiss.contains(b)).collect(Collectors.toList()));

  }

  @Test
  void testFraction_ACT_365_ACT() {
    assertEquals(181/366d,StandardDayCountConvention.ACT_365_ACT.yearFraction(
        LocalDate.of(2020,1,1),
        LocalDate.of(2020,6,30),null));
    assertEquals(180/365d,StandardDayCountConvention.ACT_365_ACT.yearFraction(
        LocalDate.of(2021,1,1),
        LocalDate.of(2021,6,30),null));
  }

  @Test
  void testFraction_ACT() {
    LOGGER.info("x={} x2={}",Frequency.P6M.getAmount(),Frequency.P6M.eventsPerMonth());
    //3a - semi-annual
    assertEquals(182/366d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(2003,11,1),
        LocalDate.of(2004,5,1),
        null));
    assertEquals(61/365d + 121/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(2003,11,1),
        LocalDate.of(2004,5,1),
        null));
    //initial
    assertEquals(182/364d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2003,11,1),
        LocalDate.of(2004,5,1),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(2003,11,1))
            .maturityDate(LocalDate.of(2008,5,1))
            .couponStartDate(LocalDate.of(2003,11,1))
            .couponEndDate(LocalDate.of(2004,5,1))
            .couponFrequency(Frequency.P6M)
            .build()));
    //final
    assertEquals(182/364d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2003,11,1),
        LocalDate.of(2004,5,1),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,11,1))
            .maturityDate(LocalDate.of(2004,5,1))
            .couponStartDate(LocalDate.of(2003,11,1))
            .couponEndDate(LocalDate.of(2004,5,1))
            .couponFrequency(Frequency.P6M)
            .build()));
    //other
    assertEquals(182/364d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2003,11,1),
        LocalDate.of(2004,5,1),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1998,11,1))
            .maturityDate(LocalDate.of(2008,5,1))
            .couponStartDate(LocalDate.of(2003,11,1))
            .couponEndDate(LocalDate.of(2004,5,1))
            .couponFrequency(Frequency.P6M)
            .build()));
    //3b - annual
    //first period
    assertEquals(150/365d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(1999,2,1),
        LocalDate.of(1999,7,1),
        null));
    assertEquals(150/365d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(1999,2,1),
        LocalDate.of(1999,7,1),
        null));
    assertEquals(150/365d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(1999,2,1),
        LocalDate.of(1999,7,1),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,2,1))
            .maturityDate(LocalDate.of(2000,7,1))
            .couponStartDate(LocalDate.of(1999,2,1))
            .couponEndDate(LocalDate.of(1999,7,1))
            .couponFrequency(Frequency.P1Y)
            .build()));
    //second period
    assertEquals(366/366d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(1999,7,1),
        LocalDate.of(2000,7,1),
        null));
    assertEquals(184/365d+182/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(1999,7,1),
        LocalDate.of(2000,7,1),
        null));
    assertEquals(366/366d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(1999,7,1),
        LocalDate.of(2000,7,1),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,2,1))
            .maturityDate(LocalDate.of(2000,7,1))
            .couponStartDate(LocalDate.of(1999,7,1))
            .couponEndDate(LocalDate.of(2000,7,1))
            .couponFrequency(Frequency.P1Y)
            .build()));
    //
    //3c
    //first period
    assertEquals(334/365d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(2002,8,15),
        LocalDate.of(2003,7,15),
        null));
    assertEquals(334/365d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(2002,8,15),
        LocalDate.of(2003,7,15),
        null));
    //assertEquals(334/365d,StandardDayCountConvention.ACT_ACT_ICMA.yearFraction(
    assertEquals(181/362d+153/368d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2002,8,15),
        LocalDate.of(2003,7,15),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(2002,8,15))
            .maturityDate(LocalDate.of(2004,1,15))
            .couponStartDate(LocalDate.of(2002,8,15))
            .couponEndDate(LocalDate.of(2003,7,15))
            .couponFrequency(Frequency.P6M)
            .build()));
    //second period
    assertEquals(184/365d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(2003,7,15),
        LocalDate.of(2004,1,15),
        null));
    assertEquals(170/365d+14/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(2003,7,15),
        LocalDate.of(2004,1,15),
        null));
    assertEquals(184/368d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2003,7,15),
        LocalDate.of(2004,1,15),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(2002,8,15))
            .maturityDate(LocalDate.of(2004,1,15))
            .couponStartDate(LocalDate.of(2003,7,15))
            .couponEndDate(LocalDate.of(2004,1,15))
            .couponFrequency(Frequency.P6M)
            .build()));
    //3d
    assertEquals(184/365d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(1999,7,30),
        LocalDate.of(2000,1,30),null));
    assertEquals(155/365d+29/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(1999,7,30),
        LocalDate.of(2000,1,30),null));
    assertEquals(184/368d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(1999,7,30),
        LocalDate.of(2000,1,30),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,7,30))
            .maturityDate(LocalDate.of(2000,6,30))
            .couponStartDate(LocalDate.of(1999,7,30))
            .couponEndDate(LocalDate.of(2000,1,30))
            .couponFrequency(Frequency.P6M)
            .build()));
    //
    assertEquals(152/366d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(2000,1,30),
        LocalDate.of(2000,6,30),
        null));
    assertEquals(152/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(2000,1,30),
        LocalDate.of(2000,6,30),
        null));
    assertEquals(152/364d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(2000,1,30),
        LocalDate.of(2000,6,30),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,7,30))
            .maturityDate(LocalDate.of(2000,6,30))
            .couponStartDate(LocalDate.of(2000,1,30))
            .couponEndDate(LocalDate.of(2000,6,30))
            .couponFrequency(Frequency.P6M)
            .build()));
    //3e
    assertEquals(152/366d,StandardDayCountConvention.ACT_ACT_AFB.yearFraction(
        LocalDate.of(1999,11,30),
        LocalDate.of(2000,4,30),
        null));
    assertEquals(32/365d+120/366d,StandardDayCountConvention.ACT_ACT_ISDA.yearFraction(
        LocalDate.of(1999,11,30),
        LocalDate.of(2000,4,30),
        null));
    assertEquals(91/364d+61/368d,StandardDayCountConvention.ACT_ACT_ISMA.yearFraction(
        LocalDate.of(1999,11,30),
        LocalDate.of(2000,4,30),
        ScheduleInfo.builder()
            .startDate(LocalDate.of(1999,11,30))
            .maturityDate(LocalDate.of(2000,4,30))
            .couponStartDate(LocalDate.of(1999,11,30))
            .couponEndDate(LocalDate.of(2000,4,30))
            .couponFrequency(Frequency.P3M)
            .build()));
  }

  @Test
  void test3() {
    LocalDate date1 = LocalDate.of(2019,03,8);
    LocalDate date2 = LocalDate.of(2020,04,21);
    LocalDate date3 = LocalDate.of(2020,05,21);
    System.out.println("XY11="+ChronoUnit.YEARS.between(date1,date2));
    System.out.println("XY12="+ChronoUnit.YEARS.between(date1,date3));
    System.out.println("XD11="+ChronoUnit.DAYS.between(date1,date2));
    System.out.println("XD12="+ChronoUnit.DAYS.between(date1,date3));
    System.out.println("XY21="+Period.between(date1,date2).getYears());
    System.out.println("XY22="+Period.between(date1,date3).getYears());
    System.out.println("XD21="+Period.between(date1,date2).getDays());
    System.out.println("XD22="+Period.between(date1,date3).getDays());
  }
}
