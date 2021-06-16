package org.blacksmith.finlib.datetime.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

import org.blacksmith.finlib.datetime.calendar.extractor.DateExtractor;
import org.blacksmith.finlib.datetime.calendar.extractor.MonthDayExtractor;
import org.blacksmith.finlib.datetime.calendar.policy.HolidayPolicyComposite;
import org.blacksmith.finlib.datetime.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.datetime.calendar.provider.DatePartInMemoryProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(org.blacksmith.test.TimingExtension.class)
public class HolidayPolicyCompositeTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(HolidayPolicyCompositeTest.class);

  private static Set<MonthDay> monthDays = Set.of(
      MonthDay.of(5, 15),
      MonthDay.of(6, 10),
      MonthDay.of(12, 25),
      MonthDay.of(12, 26));
  private static Set<LocalDate> yearMonthDays = Set.of(
      LocalDate.of(2019, 7, 15),
      LocalDate.of(2019, 9, 10));
  HolidayPolicy monthDayPolicy = new DatePartHolidayPolicy<>(MonthDayExtractor.getInstance(),
      DatePartInMemoryProvider.of(monthDays));
  HolidayPolicy yearMonthDayPolicy = new DatePartHolidayPolicy<>(DateExtractor.getInstance(),
      DatePartInMemoryProvider.of(yearMonthDays));

  @BeforeAll
  private static void setUp() {
  }

  @Test
  public void holidayPolicyCompositeBuilderTest() {
    HolidayPolicy singlePolicyComposite = HolidayPolicyComposite.builder()
        .policies(monthDayPolicy)
        .build();
    HolidayPolicy twoPoliciesComposite = HolidayPolicyComposite.builder()
        .policies(monthDayPolicy, yearMonthDayPolicy)
        .build();
    HolidayPolicy twoPoliciesComposite2 = HolidayPolicyComposite.builder()
        .policies(monthDayPolicy)
        .policies(HolidayPolicyComposite.builder().policies(yearMonthDayPolicy)
            .build())
        .build();
    checkPolicyGroup1("singlePolicyComposite", singlePolicyComposite);
    checkPolicyGroup2("twoPoliciesComposite", twoPoliciesComposite);
    checkPolicyGroup2("twoPoliciesComposite2", twoPoliciesComposite2);
  }

  @Test
  public void HolidayPolicyConstructorTest() {
    HolidayPolicy singlePolicyComposite = HolidayPolicyComposite.of(monthDayPolicy);
    HolidayPolicy twoPoliciesComposite = HolidayPolicyComposite.of(monthDayPolicy, yearMonthDayPolicy);
    HolidayPolicy[] twoPoliciesArray = { monthDayPolicy, yearMonthDayPolicy };
    HolidayPolicy twoPoliciesCompositeFromArray = HolidayPolicyComposite.of(twoPoliciesArray);
    HolidayPolicy policyChain = HolidayPolicyComposite.of(
        HolidayPolicyComposite.of(yearMonthDayPolicy),
        HolidayPolicyComposite.of(monthDayPolicy));

    checkPolicyGroup1("singlePolicyComposite", singlePolicyComposite);
    checkPolicyGroup2("twoPoliciesComposite", twoPoliciesComposite);
    checkPolicyGroup2("twoPoliciesArray", twoPoliciesCompositeFromArray);
    checkPolicyGroup2("twoPoliciesArray", policyChain);
  }

  private void checkPolicyGroup1(String chkName, HolidayPolicy policy) {
    LOGGER.info("check={}", chkName);
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
    LOGGER.info("check={}", chkName);
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
}
