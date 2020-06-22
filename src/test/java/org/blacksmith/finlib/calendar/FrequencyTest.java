package org.blacksmith.finlib.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.datetime.Frequency;
import org.junit.jupiter.api.Test;

public class FrequencyTest {
  @Test
  public void annual() {
    assertFalse(Frequency.ofDays(0).isAnnual());
    assertFalse(Frequency.ofDays(3).isAnnual());
    assertFalse(Frequency.ofWeeks(0).isAnnual());
    assertFalse(Frequency.ofWeeks(3).isAnnual());
    assertFalse(Frequency.ofMonths(0).isAnnual());
    assertFalse(Frequency.ofMonths(5).isAnnual());
    assertTrue(Frequency.ofMonths(12).isAnnual());
    assertTrue(Frequency.ofMonths(24).isAnnual());
    assertTrue(Frequency.ofYears(1).isAnnual());
    assertTrue(Frequency.ofYears(2).isAnnual());
    assertTrue(Frequency.ofYears(3).isAnnual());
  }

  @Test
  public void yearFraction() {
    assertEquals(0,Frequency.ofDays(0).eventsPerYear());
    assertEquals(0d,Frequency.ofDays(10).eventsPerYear(),0d);
    assertEquals(364d/17,Frequency.ofDays(17).eventsPerYearEstimate(),0d);
    assertEquals(1d,Frequency.ofDays(364).eventsPerYear(),0d);
    //
    assertEquals(0,Frequency.ofMonths(0).eventsPerYear());
    assertEquals(12,Frequency.ofMonths(1).eventsPerYear());
    assertEquals(12d,Frequency.ofMonths(1).eventsPerYearEstimate(),0d);
    assertEquals(4,Frequency.ofMonths(3).eventsPerYear());
    assertEquals(4d,Frequency.ofMonths(3).eventsPerYearEstimate(),0d);
    assertEquals(0,Frequency.ofMonths(5).eventsPerYear());
    assertEquals(12/5d,Frequency.ofMonths(5).eventsPerYearEstimate(),0d);
    assertEquals(2,Frequency.ofMonths(6).eventsPerYear());
    assertEquals(2d,Frequency.ofMonths(6).eventsPerYearEstimate(),0d);
    assertEquals(0,Frequency.ofMonths(9).eventsPerYear());
    assertEquals(12/9d,Frequency.ofMonths(9).eventsPerYearEstimate(),0d);
    assertEquals(1,Frequency.ofMonths(12).eventsPerYear());
    assertEquals(1d,Frequency.ofMonths(12).eventsPerYearEstimate(),0d);
    assertEquals(0,Frequency.ofMonths(14).eventsPerYear());
    assertEquals(12/14d,Frequency.ofMonths(14).eventsPerYearEstimate(),0d);
    assertEquals(0,Frequency.ofMonths(24).eventsPerYear());
    assertEquals(12/24d,Frequency.ofMonths(24).eventsPerYearEstimate(),0d);
    //
    assertEquals(0,Frequency.ofYears(0).eventsPerYear());
    assertEquals(0d,Frequency.ofYears(0).eventsPerYearEstimate(),0d);
    assertEquals(1,Frequency.ofYears(1).eventsPerYear());
    assertEquals(1d,Frequency.ofYears(1).eventsPerYearEstimate(),0d);
    assertEquals(0,Frequency.ofYears(3).eventsPerYear());
    assertEquals(1/3d,Frequency.ofYears(3).eventsPerYearEstimate(),0d);
    //
    assertEquals(0,Frequency.ofWeeks(3).eventsPerYear());
    assertEquals(364/21d,Frequency.ofWeeks(3).eventsPerYearEstimate(),0d);
    //
    assertEquals(4,new Frequency(1, TimeUnit.QUARTER).eventsPerYear());
    assertEquals(4d,new Frequency(1, TimeUnit.QUARTER).eventsPerYearEstimate(),0d);
    assertEquals(2,new Frequency(2, TimeUnit.QUARTER).eventsPerYear());
    assertEquals(2d,new Frequency(2, TimeUnit.QUARTER).eventsPerYearEstimate(),0d);
    assertEquals(0,new Frequency(3, TimeUnit.QUARTER).eventsPerYear());
    assertEquals(4/3d,new Frequency(3, TimeUnit.QUARTER).eventsPerYearEstimate(),0d);
    assertEquals(1,new Frequency(4, TimeUnit.QUARTER).eventsPerYear());
    assertEquals(1d,new Frequency(4, TimeUnit.QUARTER).eventsPerYearEstimate(),0d);
    assertEquals(0,new Frequency(6, TimeUnit.QUARTER).eventsPerYear());
    assertEquals(4/6d,new Frequency(6, TimeUnit.QUARTER).eventsPerYearEstimate(),0d);
  }

  @Test
  public void frequencyToPeriodStringConversion() {
    assertEquals("P30D",Frequency.ofDays(30).toPeriod().toString());
    assertEquals("P14D",Frequency.ofWeeks(2).toPeriod().toString());
    assertEquals("P2M",Frequency.ofMonths(2).toPeriod().toString());
    assertEquals("P2Y",Frequency.ofYears(2).toPeriod().toString());
  }

  @Test
  public void frequencyToPeriodConversion() {
    assertEquals("30D",Frequency.ofDays(30).toString());
    assertEquals("2W",Frequency.ofWeeks(2).toString());
    assertEquals("2M",Frequency.ofMonths(2).toString());
    assertEquals("2Y",Frequency.ofYears(2).toString());
  }

  @Test
  public void stringToFrequencyConversion() {
    assertEquals("P30D",Frequency.of("30D").toPeriod().toString());
    assertEquals("P14D",Frequency.of("2W").toPeriod().toString());
    assertEquals("P2M",Frequency.of("2M").toPeriod().toString());
    assertEquals("P2Y",Frequency.of("2Y").toPeriod().toString());
  }

}
