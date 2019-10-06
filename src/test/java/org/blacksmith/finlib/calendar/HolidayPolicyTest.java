package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.blacksmith.finlib.calendar.policy.ChainedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.HolidayProvider;
import org.blacksmith.finlib.calendar.policy.HolidaySetProvider;
import org.blacksmith.finlib.calendar.policy.StandardHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.WeekDaySetPolicy;
import org.blacksmith.finlib.calendar.policy.helper.StandardDateToPartConverters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(org.blacksmith.finlib.test.TimingExtension.class)
public class HolidayPolicyTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HolidayPolicyTest.class);
  
  @Test
  public void holidayByWeekDay1() {
    HolidayProvider provider = WeekDaySetPolicy.SAT_SUN;
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,25)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,26)));
  }
  @Test
  public void holidayByWeekDay2() {
    HolidayProvider provider = WeekDaySetPolicy.of(3,4);
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,16)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,5,25)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,5,26)));
  }
  @Test
  public void holidayByMonthDay() {
    Set<MonthDay> mdays = Arrays.asList(
        MonthDay.of(5,15),
        MonthDay.of(6,10),
        MonthDay.of(12,25),
        MonthDay.of(12,26)
    ).stream().collect(Collectors.toSet());
    HolidayProvider provider = HolidaySetProvider.of(StandardDateToPartConverters.MONTH_DAY,mdays);
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,12,24)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,12,25)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2020,12,26)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,12,27)));
  }
  @Test
  public void holidayByYearMonthDay() {
    Set<LocalDate> days = Arrays.asList(
        LocalDate.of(2019,5,15),
        LocalDate.of(2019,6,10)
    ).stream().collect(Collectors.toSet());
    HolidayProvider provider = HolidaySetProvider.of(StandardDateToPartConverters.YEAR_DAY,days);
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,1,15)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,5,15)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,5,20)));
    assertEquals(true,provider.isHoliday(LocalDate.of(2019,6,10)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2019,6,11)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,1,15)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,5,15)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,5,20)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,6,10)));
    assertEquals(false,provider.isHoliday(LocalDate.of(2020,6,11)));
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
    HolidayProvider provider1 = new HolidaySetProvider<MonthDay>(StandardDateToPartConverters.MONTH_DAY,hset1);
    HolidayProvider provider2 = new HolidaySetProvider<LocalDate>(StandardDateToPartConverters.YEAR_DAY,hset2);
    
    HolidayProvider[] hpa = {provider1,provider2};
    checkPolicyGroup1("chk1.1",new StandardHolidayPolicy(provider1));
    checkPolicyGroup2("chk1.2",new StandardHolidayPolicy(provider1,provider2));
    checkPolicyGroup2("chk1.3",new StandardHolidayPolicy(hpa));
    
    checkPolicyGroup1("chk2.1",ChainedHolidayPolicy.builder()
        .providers(provider1)
        .build());
    checkPolicyGroup2("chk2.2",ChainedHolidayPolicy.builder()
        .providers(provider1,provider2)
        .build());
    checkPolicyGroup2("chk2.3",ChainedHolidayPolicy.builder()
        .providers(provider1)
        .next(ChainedHolidayPolicy.builder().providers(provider2)
            .build())
        .build());

    checkPolicyGroup1("chk3.1",new CombinedHolidayPolicy(new StandardHolidayPolicy(provider1)));
    checkPolicyGroup2("chk3.2",new CombinedHolidayPolicy(new StandardHolidayPolicy(provider1),new StandardHolidayPolicy(provider2)));
    checkPolicyGroup2("chk3.3",new CombinedHolidayPolicy(new StandardHolidayPolicy(provider2),new StandardHolidayPolicy(provider1)));
}
}
