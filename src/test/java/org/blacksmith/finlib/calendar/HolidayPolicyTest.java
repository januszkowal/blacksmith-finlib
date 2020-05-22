package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.policy.ChainedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.HolidayLookupContainer;
import org.blacksmith.finlib.calendar.policy.HolidayLookupPolicy;
import org.blacksmith.finlib.calendar.policy.WeekDayPolicy;
import org.blacksmith.finlib.calendar.helper.StandardDateToPartConverters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(org.blacksmith.test.TimingExtension.class)
public class HolidayPolicyTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HolidayPolicyTest.class);
  
  @Test
  public void holidayByWeekDay1() {
    HolidayPolicy policy = WeekDayPolicy.SAT_SUN;
    assertEquals(false,policy.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,25)));
    assertEquals(true,policy.isHoliday(LocalDate.of(2019,5,26)));
  }
  @Test
  public void holidayByWeekDay2() {
    HolidayPolicy policy = WeekDayPolicy.of(3,4);
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
    HolidayPolicy policy = HolidayLookupPolicy.of(StandardDateToPartConverters.MONTH_DAY,
        HolidayLookupContainer.of(mdays));
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
    HolidayPolicy policy = HolidayLookupPolicy.of(StandardDateToPartConverters.YEAR_DAY,
        HolidayLookupContainer.of(days));
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
  
  private void checkPolicyGroup1(String chkName, HolidayPolicy policy) {
    LOGGER.info("check={}",chkName);
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
  }
  private void checkPolicyGroup2(String chkName, HolidayPolicy policy) {
    LOGGER.info("check={}",chkName);
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
    HolidayPolicy policy1 = new HolidayLookupPolicy<MonthDay>(StandardDateToPartConverters.MONTH_DAY,
        HolidayLookupContainer.of(hset1));
    HolidayPolicy policy2 = new HolidayLookupPolicy<LocalDate>(StandardDateToPartConverters.YEAR_DAY,
        HolidayLookupContainer.of(hset2));

    HolidayPolicy[] hpa = {policy1,policy2};
    checkPolicyGroup1("chk1.1",CombinedHolidayPolicy.of(policy1));
    checkPolicyGroup2("chk1.2",CombinedHolidayPolicy.of(policy1,policy2));
    checkPolicyGroup2("chk1.3",CombinedHolidayPolicy.of(hpa));

    checkPolicyGroup1("chk2.1",ChainedHolidayPolicy.builder()
        .policies(policy1)
        .build());
    checkPolicyGroup2("chk2.2",ChainedHolidayPolicy.builder()
        .policies(policy1,policy2)
        .build());
    checkPolicyGroup2("chk2.3",ChainedHolidayPolicy.builder()
        .policies(policy1)
        .next(ChainedHolidayPolicy.builder().policies(policy2)
            .build())
        .build());


    checkPolicyGroup1("chk3.1",CombinedHolidayPolicy.of(policy1));
    checkPolicyGroup1("chk3.3",CombinedHolidayPolicy.of(CombinedHolidayPolicy.of(policy1)));
    checkPolicyGroup2("chk3.4",CombinedHolidayPolicy.of(CombinedHolidayPolicy.of(policy1),CombinedHolidayPolicy.of(policy2)));
    checkPolicyGroup2("chk3.5",CombinedHolidayPolicy.of(CombinedHolidayPolicy.of(policy2),CombinedHolidayPolicy.of(policy1)));
}
}
