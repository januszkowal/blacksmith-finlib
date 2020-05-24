package org.blacksmith.finlib.calendar;

import java.time.Period;
import org.blacksmith.finlib.datetime.Frequency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrequencyTest {
  @Test
  public void testIsFrequencyAnnual() {
    assertFalse(Frequency.ofDays(0).isAnnual());
    assertFalse(Frequency.ofDays(3).isAnnual());
    //
    assertFalse(Frequency.ofWeeks(0).isAnnual());
    assertFalse(Frequency.ofWeeks(3).isAnnual());
    //
    assertFalse(Frequency.ofMonths(0).isAnnual());
    assertFalse(Frequency.ofMonths(5).isAnnual());
    assertTrue(Frequency.ofMonths(12).isAnnual());
    assertTrue(Frequency.ofMonths(24).isAnnual());
  }

  @Test
  public void fff() {
    Period period1 = Period.ofMonths(3);
    System.out.println(Frequency.ofPeriod(period1));
    Period period2 = Period.ofYears(2);
    System.out.println(Frequency.ofPeriod(period2));

  }
}
