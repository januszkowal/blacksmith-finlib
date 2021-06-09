package org.blacksmith.finlib.dayconvention;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.basis.StandardDayCounts;
import org.junit.jupiter.api.Assertions;

public class DayCountBaseTest {
  public void assertDayCountMethod(StandardDayCounts method, LocalDate d1, LocalDate d2, int expectedDays, double expectedFactor) {
    Assertions.assertEquals(expectedDays, method.days(d1, d2, null));
    Assertions.assertEquals(expectedFactor, method.yearFraction(d1, d2, null));
  }
}
