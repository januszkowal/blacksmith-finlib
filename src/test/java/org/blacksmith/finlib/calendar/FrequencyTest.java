package org.blacksmith.finlib.calendar;

import java.time.Period;
import org.blacksmith.commons.datetime.Frequency;
import org.blacksmith.commons.datetime.TimeUnit;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class FrequencyTest {
  @Test
  public void testIsFrequenceyAnnual() {
    assertEquals(false,Frequency.of(TimeUnit.DAY,0).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.DAY,3).isAnnual());
    //
    assertEquals(false,Frequency.of(TimeUnit.WEEK,0).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.WEEK,3).isAnnual());
    //
    assertEquals(false,Frequency.of(TimeUnit.MONTH,0).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.MONTH,5).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.MONTH,12).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.MONTH,24).isAnnual());
    //
    assertEquals(false,Frequency.of(TimeUnit.QUARTER,0).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.QUARTER,1).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.QUARTER,2).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.QUARTER,3).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.QUARTER,4).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.QUARTER,5).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.QUARTER,8).isAnnual());
    //
    assertEquals(false,Frequency.of(TimeUnit.HALF_YEAR,0).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.HALF_YEAR,1).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.HALF_YEAR,2).isAnnual());
    assertEquals(false,Frequency.of(TimeUnit.HALF_YEAR,3).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.HALF_YEAR,4).isAnnual());
    //
    assertEquals(false,Frequency.of(TimeUnit.YEAR,0).isAnnual());
    assertEquals(true,Frequency.of(TimeUnit.YEAR,3).isAnnual());
  }

  @Test
  public void fff() {
    Period period1 = Period.ofMonths(3);
    System.out.println(Frequency.of(period1));
    Period period2 = Period.ofYears(2);
    System.out.println(Frequency.of(period2));

  }
}
