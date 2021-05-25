package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

import org.blacksmith.finlib.basic.calendar.HolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.ChainedHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.CombinedHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.basic.calendar.policy.helper.DatePartInMemoryProvider;
import org.blacksmith.finlib.basic.calendar.policy.helper.StandardDatePartExtractors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(org.blacksmith.test.TimingExtension.class)
public class HolidayPolicyTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HolidayPolicyTest.class);


  @Test
  public void holidayByMonthDay() {
    Set<MonthDay> mdays = Set.of(
        MonthDay.of(5,15),
        MonthDay.of(6,10),
        MonthDay.of(12,25),
        MonthDay.of(12,26)
    );
    HolidayPolicy policy = DatePartHolidayPolicy.of(StandardDatePartExtractors.MONTH_DAY,
        DatePartInMemoryProvider.of(mdays));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 1, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 20)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 6, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 6, 11)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 24)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 12, 25)));
    assertTrue(policy.isHoliday(LocalDate.of(2020, 12, 26)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 27)));
  }
  @Test
  public void holidayByYearMonthDay() {
    Set<LocalDate> days = Set.of(
        LocalDate.of(2019,5,15),
        LocalDate.of(2019,6,10));
    HolidayPolicy policy = DatePartHolidayPolicy.of(StandardDatePartExtractors.DATE,
        DatePartInMemoryProvider.of(days));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 1, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 20)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 6, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 6, 11)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 1, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 5, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 5, 20)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 6, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 6, 11)));
  }

  private void checkPolicyGroup1(String chkName, HolidayPolicy policy) {
    LOGGER.info("check={}",chkName);
    assertFalse(policy.isHoliday(LocalDate.of(2019, 1, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 16)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 20)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 6, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 6, 11)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 24)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 12, 25)));
    assertTrue(policy.isHoliday(LocalDate.of(2020, 12, 26)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 27)));
  }
  private void checkPolicyGroup2(String chkName, HolidayPolicy policy) {
    LOGGER.info("check={}",chkName);
    assertFalse(policy.isHoliday(LocalDate.of(2019, 1, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 16)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 20)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 6, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 6, 11)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 7, 15)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 7, 20)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 9, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 9, 10)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 24)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 12, 25)));
    assertTrue(policy.isHoliday(LocalDate.of(2020, 12, 26)));
    assertFalse(policy.isHoliday(LocalDate.of(2020, 12, 27)));
  }
  @Test
  public void holidayPolicyGroup() {
    Set<MonthDay> hset1 = Set.of(
        MonthDay.of(5,15),
        MonthDay.of(6,10),
        MonthDay.of(12,25),
        MonthDay.of(12,26));
    Set<LocalDate> hset2 = Set.of(
        LocalDate.of(2019,7,15),
        LocalDate.of(2019,9,10)
    );
    HolidayPolicy policy1 = new DatePartHolidayPolicy<>(StandardDatePartExtractors.MONTH_DAY,
        DatePartInMemoryProvider.of(hset1));
    HolidayPolicy policy2 = new DatePartHolidayPolicy<>(StandardDatePartExtractors.DATE,
        DatePartInMemoryProvider.of(hset2));

    HolidayPolicy[] hpa = {policy1,policy2};
    checkPolicyGroup1("chk1.1", CombinedHolidayPolicy.of(policy1));
    checkPolicyGroup2("chk1.2",CombinedHolidayPolicy.of(policy1,policy2));
    checkPolicyGroup2("chk1.3",CombinedHolidayPolicy.of(hpa));

    checkPolicyGroup1("chk2.1", ChainedHolidayPolicy.builder()
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
