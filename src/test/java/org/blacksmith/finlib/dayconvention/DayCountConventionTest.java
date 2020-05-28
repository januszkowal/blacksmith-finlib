package org.blacksmith.finlib.dayconvention;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.DayCountConvention;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;
import org.junit.jupiter.api.Test;

class DayCountConventionTest {

  @Test
  public void testDayCountConvention() {
    DayCountConvention dayCountConvention = StandardDayCountConvention.D30_360_ISDA;
    assertEquals(30,dayCountConvention.days(LocalDate.parse("2020-03-01"),LocalDate.parse("2020-04-01")));
    assertEquals(0.08333333333333333,dayCountConvention.relativeYearFraction(LocalDate.parse("2020-03-01"),LocalDate.parse("2020-04-01"),null));

    dayCountConvention = StandardDayCountConvention.ACT_365_25;
    assertEquals(31,dayCountConvention.days(LocalDate.parse("2020-03-01"),LocalDate.parse("2020-04-01")));
    assertEquals(0.08487337440109514,dayCountConvention.relativeYearFraction(LocalDate.parse("2020-03-01"),LocalDate.parse("2020-04-01"),null));
  }

}