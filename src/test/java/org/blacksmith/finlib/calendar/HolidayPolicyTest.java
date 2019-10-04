package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.MonthDaySetPolicy;
import org.blacksmith.finlib.calendar.policy.WeekDaySetPolicy;
import org.blacksmith.finlib.calendar.policy.YearMonthDaySetPolicy;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class HolidayPolicyTest {
  @Test
  public void holidayByWeekDay1() {
    HolidayPolicy policy = WeekDaySetPolicy.SAT_SUN;
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,25)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,26)));
  }
  @Test
  public void holidayByWeekDay2() {
    HolidayPolicy policy = new WeekDaySetPolicy(new int[]{3,4});
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,16)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,25)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,26)));
  }
  @Test
  public void holidayByMonthDay() {
    Set<MonthDay> mdays = Arrays.asList(
        MonthDay.of(5,15),
        MonthDay.of(6,10),
        MonthDay.of(12,25),
        MonthDay.of(12,26)
    ).stream().collect(Collectors.toSet());
    HolidayPolicy policy = new MonthDaySetPolicy(mdays);
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,24)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,12,25)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2020,12,26)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,27)));
  }
  @Test
  public void holidayByYearMonthDay() {
    Set<LocalDate> days = Arrays.asList(
        LocalDate.of(2019,5,15),
        LocalDate.of(2019,6,10)
    ).stream().collect(Collectors.toSet());
    HolidayPolicy policy = new YearMonthDaySetPolicy(days);
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,1,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,5,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,5,20)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,6,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,6,11)));
  }
  @Test
  public void holidayPolicyGroup() {
    Set<MonthDay> hset1 = Arrays.asList(
        MonthDay.of(5,15),
        MonthDay.of(6,10),
        MonthDay.of(12,25),
        MonthDay.of(12,26)
    ).stream().collect(Collectors.toSet());
    Set<LocalDate> hset2 = Arrays.asList(
        LocalDate.of(2019,7,15),
        LocalDate.of(2019,9,10)
    ).stream().collect(Collectors.toSet());
    HolidayPolicy policy1 = new MonthDaySetPolicy(hset1);
    HolidayPolicy policy2 = new YearMonthDaySetPolicy(hset2);

    CombinedHolidayPolicy policy = null;

    policy = new CombinedHolidayPolicy(Arrays.asList(policy1));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,16)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,24)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,12,25)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2020,12,26)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,27)));

    policy = new CombinedHolidayPolicy(Arrays.asList(policy1,policy2));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,16)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,7,15)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,7,20)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,9,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,9,10)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,24)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,12,25)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2020,12,26)));
    assertEquals(false,policy.isHoliday(LocalDate.of(2020,12,27)));
  }
}
