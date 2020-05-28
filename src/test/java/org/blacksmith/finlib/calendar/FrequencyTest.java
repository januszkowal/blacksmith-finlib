package org.blacksmith.finlib.calendar;

import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.datetime.Frequency;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
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
    assertTrue(Frequency.ofYears(1).isAnnual());
    assertTrue(Frequency.ofYears(2).isAnnual());
    assertTrue(Frequency.ofYears(3).isAnnual());
  }

  @Test
  public void eventsPerMonthTest() {
    assertEquals(30d*12/365,Frequency.ofDays(30).eventsPerMonth(),0.0);
    assertEquals(1d*12/365,Frequency.ofDays(1).eventsPerMonth(),0.0);
    assertEquals(3d*12/365,Frequency.ofDays(3).eventsPerMonth(),0.0);
    assertEquals(14d*12/365,Frequency.ofDays(14).eventsPerMonth(),0.0);
    assertEquals(15d*12/365,Frequency.ofDays(15).eventsPerMonth(),0.0);
    assertEquals(28d*12/365,Frequency.ofDays(28).eventsPerMonth(),0.0);

    assertEquals(1.0d,Frequency.ofMonths(1).eventsPerMonth(),0.0);
    assertEquals(0.5d,Frequency.ofMonths(2).eventsPerMonth(),0.0);
    assertEquals(1.0d/3,Frequency.ofMonths(3).eventsPerMonth(),0.0);
    assertEquals(0.25,Frequency.ofMonths(4).eventsPerMonth(),0.0);

    assertEquals(1.0d/6,new Frequency(1, TimeUnit.HALF_YEAR).eventsPerMonth(),0.0);
    assertEquals(1.0d/12,new Frequency(2, TimeUnit.HALF_YEAR).eventsPerMonth(),0.0);
    assertEquals(1.0d/18,new Frequency(3, TimeUnit.HALF_YEAR).eventsPerMonth(),0.0);
    assertEquals(1.0d/24,new Frequency(4, TimeUnit.HALF_YEAR).eventsPerMonth(),0.0);

    assertEquals(1.0d/12,Frequency.ofYears(1).eventsPerMonth(),0.0);
    assertEquals(1.0d/24,Frequency.ofYears(2).eventsPerMonth(),0.0);
  }

  @Test
  public void eventsPerMonthIntTest() {
    assertEquals(0,Frequency.ofDays(30).eventsPerMonthInt());
    assertEquals(1,Frequency.ofDays(31).eventsPerMonthInt());
    assertEquals(1,Frequency.ofMonths(1).eventsPerMonthInt());
    assertEquals(0,Frequency.ofMonths(2).eventsPerMonthInt());
  }

  @Test
  public void toPeriodsTest() {
    assertEquals("P30D",Frequency.ofDays(30).toPeriod().toString());
    assertEquals("P14D",Frequency.ofWeeks(2).toPeriod().toString());
    assertEquals("P2M",Frequency.ofMonths(2).toPeriod().toString());
    assertEquals("P2Y",Frequency.ofYears(2).toPeriod().toString());
  }

  @Test
  public void toStringTest() {
    assertEquals("P30D",Frequency.ofDays(30).toString());
    assertEquals("P2W",Frequency.ofWeeks(2).toString());
    assertEquals("P2M",Frequency.ofMonths(2).toString());
    assertEquals("P2Y",Frequency.ofYears(2).toString());
  }

}
