package org.blacksmith.finlib.calendar;

import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Assertions;
import java.time.LocalDate;

@Slf4j
public class DayCountTest extends DayCountBaseTest {

  public void assertDayCountMethod(StandardDayCountConvention method, String s1, String s2, int expectedDays, double expectedFactor) {
    LocalDate d1 = LocalDate.parse(s1, DateTimeFormatter.BASIC_ISO_DATE);
    LocalDate d2 = LocalDate.parse(s2, DateTimeFormatter.BASIC_ISO_DATE);
    Assertions.assertEquals(expectedDays,method.days(d1,d2,null));
    Assertions.assertEquals(expectedFactor,method.yearFraction(d1,d2,null));
  }
}
