package org.blacksmith.finlib.datetime;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Period;
import java.time.temporal.Temporal;
import java.util.Locale;
import org.blacksmith.commons.datetime.DateOperation;
import org.blacksmith.commons.datetime.TimeUnit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Frequency implements Serializable, DateOperation {
  @EqualsAndHashCode.Include
  @ToString.Include
  private final TimeUnit unit;
  @EqualsAndHashCode.Include
  @ToString.Include
  private final int amount;
  @ToString.Include
  private final String name;
  private final Period period;

  /**
   * Serialization version.
   */
  private static final long serialVersionUID = 1;
  /**
   * The artificial maximum length of a normal tenor in years.
   */
  private static final int MAX_YEARS = 1_000;
  /**
   * The artificial maximum length of a normal tenor in months.
   */
  private static final int MAX_MONTHS = MAX_YEARS * 12;
  /**
   * The artificial length in years of the 'Term' frequency.
   */
  private static final int TERM_YEARS = 10_000;

  /**
   * A periodic frequency of one day.
   * Also known as daily.
   * There are considered to be 364 events per year with this frequency.
   */
  public static final Frequency P1D = new Frequency(1, TimeUnit.DAY);
  /**
   * A periodic frequency of 1 week (7 days).
   * Also known as weekly.
   * There are considered to be 52 events per year with this frequency.
   */
  public static final Frequency P1W = new Frequency(1, TimeUnit.WEEK);
  /**
   * A periodic frequency of 2 weeks (14 days).
   * Also known as bi-weekly.
   * There are considered to be 26 events per year with this frequency.
   */
  public static final Frequency P2W = new Frequency(2, TimeUnit.WEEK);
  /**
   * A periodic frequency of 4 weeks (28 days).
   * Also known as lunar.
   * There are considered to be 13 events per year with this frequency.
   */
  public static final Frequency P4W = new Frequency(4, TimeUnit.WEEK);
  /**
   * A periodic frequency of 13 weeks (91 days).
   * There are considered to be 4 events per year with this frequency.
   */
  public static final Frequency P13W = new Frequency(13, TimeUnit.WEEK);
  /**
   * A periodic frequency of 26 weeks (182 days).
   * There are considered to be 2 events per year with this frequency.
   */
  public static final Frequency P26W = new Frequency(26, TimeUnit.WEEK);
  /**
   * A periodic frequency of 52 weeks (364 days).
   * There is considered to be 1 event per year with this frequency.
   */
  public static final Frequency P52W = new Frequency(52, TimeUnit.WEEK);
  /**
   * A periodic frequency of 1 month.
   * Also known as monthly.
   * There are 12 events per year with this frequency.
   */
  public static final Frequency P1M = new Frequency(1, TimeUnit.MONTH);
  /**
   * A periodic frequency of 2 months.
   * Also known as bi-monthly.
   * There are 6 events per year with this frequency.
   */
  public static final Frequency P2M = new Frequency(2, TimeUnit.MONTH);
  /**
   * A periodic frequency of 3 months.
   * Also known as quarterly.
   * There are 4 events per year with this frequency.
   */
  public static final Frequency P3M = new Frequency(3, TimeUnit.MONTH);
  /**
   * A periodic frequency of 4 months.
   * There are 3 events per year with this frequency.
   */
  public static final Frequency P4M = new Frequency(4, TimeUnit.MONTH);
  /**
   * A periodic frequency of 6 months.
   * Also known as semi-annual.
   * There are 2 events per year with this frequency.
   */
  public static final Frequency P6M = new Frequency(6, TimeUnit.MONTH);
  /**
   * A periodic frequency of 9 months.
   * Also known as semi-annual.
   * There are 2 events per year with this frequency.
   */
  public static final Frequency P9M = new Frequency(9, TimeUnit.MONTH);
  /**
   * A periodic frequency of 12 months (1 year).
   * Also known as annual.
   * There is 1 event per year with this frequency.
   */
  public static final Frequency P1Y = new Frequency(1, TimeUnit.YEAR);
  /**
   * A periodic frequency of 2 years.
   * Also known as zero-coupon.
   * This is represented using the period 10,000 years.
   * There are no events per year with this frequency.
   */
  public static final Frequency P2Y = new Frequency(2, TimeUnit.YEAR);
  /**
   * A periodic frequency of 3 years.
   * Also known as zero-coupon.
   * This is represented using the period 10,000 years.
   * There are no events per year with this frequency.
   */
  public static final Frequency P3Y = new Frequency(3, TimeUnit.YEAR);
  /**
   * A periodic frequency of 5 years.
   * Also known as zero-coupon.
   * This is represented using the period 10,000 years.
   * There are no events per year with this frequency.
   */
  public static final Frequency P5Y = new Frequency(5, TimeUnit.YEAR);
  /**
   * A periodic frequency matching the term.
   * Also known as zero-coupon.
   * This is represented using the period 10,000 years.
   * There are no events per year with this frequency.
   */
  public static final Frequency TERM = new Frequency(TERM_YEARS, TimeUnit.YEAR, "TERM");

  public Frequency(final Period period) {
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
    this.period = frequencyToPeriod(this.amount,this.unit);
    this.name = periodName(this.amount,this.unit);
  }

  public Frequency (final int amount, final TimeUnit unit) {
    this.unit = unit;
    this.amount = amount;
    this.period = frequencyToPeriod(this.amount,this.unit);
    this.name = periodName(this.amount,this.unit);
  }

  public Frequency (final int amount, final TimeUnit unit, String name) {
    this.unit = unit;
    this.amount = amount;
    this.period = frequencyToPeriod(amount,unit);
    this.name = name;
  }

  public static Frequency ofPeriod(Period period) {
    return new Frequency(period);
  }

  public static Frequency ofDays(int days) {
    if (days % 7 == 0) {
      return ofWeeks(days / 7);
    }
    else {
      return new Frequency(days,TimeUnit.DAY);
    }
  }

  /**
   * Obtains an instance backed by a period of weeks.
   *
   * @param weeks  the number of weeks
   * @return the periodic frequency
   * @throws IllegalArgumentException if weeks is negative or zero
   */
  public static Frequency ofWeeks(int weeks) {
    switch (weeks) {
      case 1:
        return P1W;
      case 2:
        return P2W;
      case 4:
        return P4W;
      case 13:
        return P13W;
      case 26:
        return P26W;
      case 52:
        return P52W;
      default:
        return new Frequency(weeks,TimeUnit.WEEK);
    }
  }

  /**
   * Obtains an instance backed by a period of months.
   * <p>
   * Months are not normalized into years.
   *
   * @param months  the number of months
   * @return the periodic frequency
   * @throws IllegalArgumentException if months is negative, zero or over 12,000
   */
  public static Frequency ofMonths(int months) {
    if (months % 12 == 0) {
      return ofYears(months/12);
    }
    else {
      switch (months) {
        case 1:
          return P1M;
        case 2:
          return P2M;
        case 3:
          return P3M;
        case 4:
          return P4M;
        case 6:
          return P6M;
        default:
          if (months > MAX_MONTHS) {
            throw new IllegalArgumentException(maxMonthMsg());
          }
          return new Frequency(months,TimeUnit.MONTH);
      }
    }
  }

  /**
   * Obtains an instance backed by a period of years.
   *
   * @param years  the number of years
   * @return the periodic frequency
   * @throws IllegalArgumentException if years is negative, zero or over 1,000
   */
  public static Frequency ofYears(int years) {
    switch (years) {
      case 1:
        return P1Y;
      case 2:
        return P2Y;
      case 3:
        return P3Y;
      case 5:
        return P5Y;
      default:
        if (years > MAX_YEARS) {
          throw new IllegalArgumentException(maxYearMsg());
        }
        return new Frequency(years, TimeUnit.YEAR);
    }
  }

  // extracted to aid inlining
  private static String maxYearMsg() {
    DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.ENGLISH));
    return "Years must not exceed " + formatter.format(MAX_YEARS);
  }

  // extracted to aid inlining
  private static String maxMonthMsg() {
    DecimalFormat formatter = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.ENGLISH));
    return "Months must not exceed " + formatter.format(MAX_MONTHS);
  }


  public boolean isAnnual() {
    return (amount!=0)&&(
        (unit==TimeUnit.YEAR) ||
        (unit==TimeUnit.MONTH && amount%12==0) ||
        (unit==TimeUnit.QUARTER && amount%4==0) ||
        (unit==TimeUnit.HALF_YEAR && amount%2==0));
  }

  public Period toPeriod() {
    return frequencyToPeriod(this.amount,this.unit);
  }

  public static Period frequencyToPeriod(final int amount, final TimeUnit unit) {
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

  public double eventsPerYear() {
    switch (unit) {
      case MONTH:
        return 12d / amount;
      case QUARTER:
        return 4d / amount;
      case HALF_YEAR:
        return 2d / amount;
      case YEAR:
        return 1d / amount;
      default:
        return 0;
    }
  }

  public int eventsPerMonth() {
    switch (unit) {
      case MONTH:
        return amount;
      case QUARTER:
        return amount/3;
      case HALF_YEAR:
        return amount/6;
      case YEAR:
        return amount/12;
      default:
        return 0;
    }
  }

  private String periodName(final int amount, final TimeUnit unit) {
    return amount + unit.symbol();
  }

  @Override
  public <T extends Temporal> T addTo(T t, int i) {
    return this.unit.addTo(t,i*amount);
  }

  @Override
  public <T extends Temporal> T minusFrom(T t, int i) {
    return this.unit.minusFrom(t,i*amount);
  }

//  @Override
//  public <R extends Temporal> R plus(R t, int amount) {
//    return this.unit.plus(t,this.amount*amount);
//  }
//
//  @Override
//  public <R extends Temporal> R plus(R t) {
//    return this.unit.plus(t,this.amount);
//  }
//
//  @Override
//  public <R extends Temporal> R minus(R t, int amount) {
//    return this.unit.plus(t,this.amount*amount);
//  }
//
//  @Override
//  public <R extends Temporal> R minus(R t) {
//    return this.unit.plus(t,this.amount);
//  }
}
