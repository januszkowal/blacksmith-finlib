package org.blacksmith.finlib.datetime.calendar;

import java.time.LocalDate;
import java.util.Set;

import org.blacksmith.finlib.datetime.calendar.extractor.DateExtractor;
import org.blacksmith.finlib.datetime.calendar.policy.DatePartHolidayPolicy;
import org.blacksmith.finlib.datetime.calendar.provider.DatePartInMemoryProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YearMonthDayPolicyTest {
  @Test
  public void holidayByYearMonthDay() {
    Set<LocalDate> days = Set.of(
        LocalDate.of(2019, 5, 15),
        LocalDate.of(2019, 6, 10));
    HolidayPolicy policy = DatePartHolidayPolicy.of(DateExtractor.getInstance(),
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
}

