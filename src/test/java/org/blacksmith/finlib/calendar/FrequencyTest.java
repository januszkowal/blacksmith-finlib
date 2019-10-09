package org.blacksmith.finlib.calendar;

import java.time.Period;
import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.basic.Frequency;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrequencyTest {
  @Test
  public void testIsFrequenceyAnnual() {
    assertEquals(false,Frequency.ofDays(0).isAnnual());
    assertEquals(false,Frequency.ofDays(3).isAnnual());
    //
    assertEquals(false,Frequency.ofWeeks(0).isAnnual());
    assertEquals(false,Frequency.ofWeeks(3).isAnnual());
    //
    assertEquals(false,Frequency.ofMonths(0).isAnnual());
    assertEquals(false,Frequency.ofMonths(5).isAnnual());
    assertEquals(true,Frequency.ofMonths(12).isAnnual());
    assertEquals(true,Frequency.ofMonths(24).isAnnual());
  }

  @Test
  public void fff() {
    Period period1 = Period.ofMonths(3);
    System.out.println(Frequency.ofPeriod(period1));
    Period period2 = Period.ofYears(2);
    System.out.println(Frequency.ofPeriod(period2));

  }
}
