package org.blacksmith.finlib.datetime.calendar;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.calendar.policy.StandardWeekDayPolicy;
import org.blacksmith.finlib.datetime.calendar.policy.WeekDayPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeekDayPolicyTest {

  @Test
  public void standardWeekDayPolicyTest() {
    HolidayPolicy policy = StandardWeekDayPolicy.SAT_SUN;
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 25)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 26)));
  }

  @Test
  public void userDefinedWeekDayPolicyTest() {
    HolidayPolicy policy = WeekDayPolicy.of(3,4);
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 15)));
    assertTrue(policy.isHoliday(LocalDate.of(2019, 5, 16)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 25)));
    assertFalse(policy.isHoliday(LocalDate.of(2019, 5, 26)));
  }
}
