package org.blacksmith.finlib.datetime.calendar;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.Set;

import org.blacksmith.finlib.datetime.calendar.extractor.MonthDayExtractor;
import org.blacksmith.finlib.datetime.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.datetime.calendar.provider.DatePartInMemoryProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MonthDayPolicyTest {
  @Test
  public void holidayByMonthDay() {
    Set<MonthDay> monthDays = Set.of(
        MonthDay.of(5, 15),
        MonthDay.of(6, 10),
        MonthDay.of(12, 25),
        MonthDay.of(12, 26)
    );
    HolidayPolicy policy = DatePartHolidayPolicy.of(MonthDayExtractor.getInstance(),
        DatePartInMemoryProvider.of(monthDays));
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
}
