package org.blacksmith.finlib.calendar;

import java.time.LocalDate;
import org.blacksmith.finlib.calendar.policy.DefaultHolidayPolicy;
import org.blacksmith.finlib.calendar.policy.YearMonthDayPolicySet;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class BusinessDayCalendarTest {
  @Test
  public void holidayByWeekDay1() {
    /*
    holidays: 2019-05-15, 2019-05-16, 2019-05-17
    * */
    YearMonthDayPolicySet ymdPolicy = new YearMonthDayPolicySet();
    ymdPolicy.add(LocalDate.of(2019,5,15));
    ymdPolicy.add(LocalDate.of(2019,5,16));
    ymdPolicy.add(LocalDate.of(2019,6,15));
    BusinessDayCalendar cal = new BusinessDayCalendarWithPolicy(ymdPolicy);
    assertEquals(LocalDate.of(2019,05,14),cal.nextOrSame(LocalDate.of(2019,5,14)));
    assertEquals(LocalDate.of(2019,05,17),cal.nextOrSame(LocalDate.of(2019,5,15)));
    assertEquals(LocalDate.of(2019,05,17),cal.nextOrSame(LocalDate.of(2019,5,16)));
    assertEquals(LocalDate.of(2019,06,16),cal.nextOrSame(LocalDate.of(2019,6,15)));
    assertEquals(LocalDate.of(2019,06,16),cal.nextOrSame(LocalDate.of(2019,6,16)));
    assertEquals(LocalDate.of(2019,06,17),cal.nextOrSame(LocalDate.of(2019,6,17)));
    //
    assertEquals(LocalDate.of(2019,05,14),cal.next(LocalDate.of(2019,5,13)));
    assertEquals(LocalDate.of(2019,05,17),cal.next(LocalDate.of(2019,5,14)));
    assertEquals(LocalDate.of(2019,05,17),cal.next(LocalDate.of(2019,5,15)));
    assertEquals(LocalDate.of(2019,06,16),cal.next(LocalDate.of(2019,6,15)));
    //
    assertEquals(LocalDate.of(2019,05,14),cal.next(LocalDate.of(2019,5,13),1));
    assertEquals(LocalDate.of(2019,05,17),cal.next(LocalDate.of(2019,5,14),1));
    assertEquals(LocalDate.of(2019,05,17),cal.next(LocalDate.of(2019,5,15),1));
    assertEquals(LocalDate.of(2019,06,16),cal.next(LocalDate.of(2019,6,15),1));
    //
    assertEquals(LocalDate.of(2019,05,13),cal.previous(LocalDate.of(2019,5,14)));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,15)));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,16)));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,17)));
    assertEquals(LocalDate.of(2019,06,17),cal.previous(LocalDate.of(2019,6,18)));
    //
    assertEquals(LocalDate.of(2019,05,13),cal.previous(LocalDate.of(2019,5,14),1));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,15),1));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,16),1));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,17),1));
    assertEquals(LocalDate.of(2019,06,17),cal.previous(LocalDate.of(2019,6,18),1));
    //
    assertEquals(LocalDate.of(2019,05,11),cal.next(LocalDate.of(2019,5,10),1));
    assertEquals(LocalDate.of(2019,05,12),cal.next(LocalDate.of(2019,5,10),2));
    assertEquals(LocalDate.of(2019,05,13),cal.next(LocalDate.of(2019,5,10),3));
    //
    assertEquals(LocalDate.of(2019,05,14),cal.next(LocalDate.of(2019,5,13),1));
    assertEquals(LocalDate.of(2019,05,17),cal.next(LocalDate.of(2019,5,13),2));
    assertEquals(LocalDate.of(2019,05,18),cal.next(LocalDate.of(2019,5,13),3));
    //
    assertEquals(LocalDate.of(2019,05,9),cal.previous(LocalDate.of(2019,5,10),1));
    assertEquals(LocalDate.of(2019,05,8),cal.previous(LocalDate.of(2019,5,10),2));
    assertEquals(LocalDate.of(2019,05,7),cal.previous(LocalDate.of(2019,5,10),3));
    //
    assertEquals(LocalDate.of(2019,05,17),cal.previous(LocalDate.of(2019,5,18),1));
    assertEquals(LocalDate.of(2019,05,14),cal.previous(LocalDate.of(2019,5,18),2));
    assertEquals(LocalDate.of(2019,05,13),cal.previous(LocalDate.of(2019,5,18),3));
    //
    assertEquals(LocalDate.of(2019,05,11),cal.shift(LocalDate.of(2019,5,10),1));
    assertEquals(LocalDate.of(2019,05,12),cal.shift(LocalDate.of(2019,5,10),2));
    assertEquals(LocalDate.of(2019,05,13),cal.shift(LocalDate.of(2019,5,10),3));
    assertEquals(LocalDate.of(2019,05,17),cal.shift(LocalDate.of(2019,5,18),-1));
    assertEquals(LocalDate.of(2019,05,14),cal.shift(LocalDate.of(2019,5,18),-2));
    assertEquals(LocalDate.of(2019,05,13),cal.shift(LocalDate.of(2019,5,18),-3));
  }
}
