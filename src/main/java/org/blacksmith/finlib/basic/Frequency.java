package org.blacksmith.finlib.basic;

import java.sql.Time;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
//@AllArgsConstructor(staticName="of")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Frequency implements TemporalAmount {
  @EqualsAndHashCode.Include
  @ToString.Include
  private final TimeUnit unit;
  @EqualsAndHashCode.Include
  @ToString.Include
  private final int amount;
  //@EqualsAndHashCode.Include
  @ToString.Include
  private final Period period;

  public Frequency(TimeUnit unit, int amount) {
    this.unit = unit;
    this.amount = amount;
    this.period = frequencyToPeriod(unit,amount);
  }

  public Frequency(Period period) {
    final List<TemporalUnit> u = period.getUnits();
    if (period.getYears() > 0) {
      this.unit = TimeUnit.YEAR;
      this.amount = period.getYears();
    }
    else if (period.getMonths() > 0) {
      this.unit = TimeUnit.MONTH;
      this.amount = period.getMonths();
    }
    else {
      this.unit = TimeUnit.DAY;
      this.amount = period.getDays();
    }
    this.period = frequencyToPeriod(this.unit,this.amount);
  }

  public static Frequency of(TimeUnit unit, int amount) {
    return new Frequency(unit,amount);
  }

  public static Frequency of(Period period) {
    return new Frequency(period);
  }

  public boolean isAnnual() {
    return (amount!=0)&&((unit==TimeUnit.YEAR) ||
        (unit==TimeUnit.MONTH && amount%12==0) ||
        (unit==TimeUnit.QUARTER && amount%4==0) ||
        (unit==TimeUnit.HALF_YEAR && amount%2==0));
  }

  public Period toPeriod() {
    return Frequency.frequencyToPeriod(this.unit,this.amount);
  }

  public static Period frequencyToPeriod(TimeUnit unit, int amount) {
    switch (unit) {
      case DAY:
        return Period.ofDays(amount);
      case WEEK:
        return Period.ofWeeks(amount);
      case MONTH:
        return Period.ofMonths(amount);
      case QUARTER:
        return Period.ofMonths(amount*3);
      case HALF_YEAR:
        return Period.ofMonths(amount*6);
      case YEAR:
        return Period.ofYears(amount);
      default:
        return null;
    }
  }

  public int eventsPerYear() {
    switch (unit) {
      case MONTH:
        return amount / 12;
      case QUARTER:
        return amount / 4;
      case HALF_YEAR:
        return amount / 2;
      case YEAR:
        return amount;
      default:
        return 0;
    }
  }

  public int eventsPerMonth() {
    switch (unit) {
      case MONTH:
        return amount;
      case QUARTER:
        return amount*3;
      case HALF_YEAR:
        return amount*6;
      case YEAR:
        return amount*12;
      default:
        return 0;
    }
  }

  @Override public long get(TemporalUnit temporalUnit) {
    return 0;
  }

  @Override public List<TemporalUnit> getUnits() {
    return period.getUnits();
  }

  @Override public Temporal addTo(Temporal temporal) {
    return period.addTo(temporal);
  }

  @Override public Temporal subtractFrom(Temporal temporal) {
    return period.subtractFrom(temporal);
  }
 //  private final TimeUnit unit;
//  private final int amount;
//
//  public
//
//  @Override public LocalDate plus(LocalDate date, int amount) {
//    return unit.plus(date,amount);
//  }
//
//  @Override public LocalDate minus(LocalDate date, int amount) {
//    return this.unit.minus(date,amount);
//  }
}
