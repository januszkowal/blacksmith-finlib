package org.blacksmith.finlib.calendar;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateRange;
import org.blacksmith.finlib.calendar.policy.StandardWeekDayPolicy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusinessDayCountTest {
  @Test
  public void dayCountTest() {
    BusinessDayCalendar noHolidaysCalendar = BusinessDayCalendarWithPolicy.of((d)->false);
    BusinessDayCalendar weekendCalendar = BusinessDayCalendarWithPolicy.of(StandardWeekDayPolicy.SAT_SUN);
    assertEquals(30,noHolidaysCalendar.businessDaysCount(DateRange.closedOpen(LocalDate.of(2019, 1, 1),LocalDate.of(2019,
        1,31))));
    assertEquals(31,noHolidaysCalendar.businessDaysCount(DateRange.closed(LocalDate.of(2019, 1, 1),LocalDate.of(2019, 1,31))));
    assertEquals(22,weekendCalendar.businessDaysCount(DateRange.closedOpen(LocalDate.of(2019, 1, 1),LocalDate.of(2019,
        1,31))));
    assertEquals(23,weekendCalendar.businessDaysCount(DateRange.closed(LocalDate.of(2019, 1, 1),LocalDate.of(2019, 1,31))));
  }
}
