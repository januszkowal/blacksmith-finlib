package org.blacksmith.finlib.datetime;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Locale;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.commons.datetime.DateOperation;
import org.blacksmith.commons.datetime.TimeUnit;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Frequency implements Serializable, DateOperation {
  @EqualsAndHashCode.Include
  private final TimeUnit unit;
  @EqualsAndHashCode.Include
  private final int amount;
  @ToString.Include
  private String name;
  boolean isAnnual;
  private int months;
  private int eventsPerYear;
  private double eventsPerYearEstimate;
//  private final Period period;

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
    //TODO multi-unit periods
    if (period.getUnits().size()>0 && period.getDays()>0) {
      throw new IllegalArgumentException("Multiple unit period not supported");
    }
    if (period.getYears() > 0) {
      if (period.getMonths()==0) {
        this.unit = TimeUnit.YEAR;
        this.amount = period.getYears();
      }
      else {
        this.unit = TimeUnit.MONTH;
        this.amount = (int)period.toTotalMonths();
      }
    } else if (period.getMonths() > 0) {
      this.unit = TimeUnit.MONTH;
      this.amount = period.getMonths();
    } else {
      this.unit = TimeUnit.DAY;
      this.amount = period.getDays();
    }
    setEvents();
  }

  public Frequency (final int amount, final TimeUnit unit) {
    this.unit = unit;
    this.amount = amount;
    this.name = periodName(this.amount,this.unit);
    setEvents();
  }

  private void setEvents() {
    if (amount==0) {
      return;
    }
    double secs = 0d;
//    this.period = frequencyToPeriod(this.amount,this.unit);
    switch(unit) {
      case DAY:
        secs = amount * ChronoUnit.DAYS.getDuration().getSeconds();
        eventsPerYear = (364 % amount == 0) ? 364 / amount : 0;
        eventsPerYearEstimate = 364d / amount;
        //eventsPerYearEstimate = ChronoUnit.YEARS.getDuration().getSeconds() / secs;
        break;
      case WEEK:
        secs = amount * ChronoUnit.WEEKS.getDuration().getSeconds();
        int days = amount * 7;
        eventsPerYear = (364 % days == 0) ? 364 / days : 0;
        eventsPerYearEstimate = 364d / days;
        //eventsPerYearEstimate = ChronoUnit.YEARS.getDuration().getSeconds() / secs;
        break;
      case MONTH:
        isAnnual = amount % 12 ==0;
        eventsPerYear = (12 % amount == 0) ? 12 / amount : 0;
        eventsPerYearEstimate = 12d / amount;
        months = amount;
        break;
      case QUARTER:
        isAnnual = amount % 4 ==0;
        eventsPerYear = (4 % amount == 0) ? 4 / amount : 0;
        eventsPerYearEstimate = 4d / amount;
        months = amount * 3;
        break;
      case HALF_YEAR:
        isAnnual = amount % 2 ==0;
        eventsPerYear = (2 % amount == 0) ? 2 / amount : 0;
        eventsPerYearEstimate = 2d / amount;
        months = amount * 6;
        break;
      case YEAR:
        isAnnual = true;
        eventsPerYear = (1 % amount == 0) ? 1 / amount : 0;
        eventsPerYearEstimate = 1d / amount;
        months = amount * 12;
        break;
    }
  }

  public Frequency (final int amount, final TimeUnit unit, String name) {
    this.unit = unit;
    this.amount = amount;
    this.name = name;
  }

  public static Frequency of(String period) {
    ArgChecker.checkStringLength(period,2,10);
    int amount = Integer.parseInt(period,0,period.length()-1,10);
    TimeUnit unit = TimeUnit.ofSymbol(period.substring(period.length()-1));
    return new Frequency(amount,unit);
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

  public static Frequency ofDaysWithoutAlignToWeeks(int days) {
    return new Frequency(days,TimeUnit.DAY);
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
    return isAnnual;
  }

  public int getMonths() {
    return  this.months;
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

  @Override
  public String toString() {
    return String.format("%d%s",amount,unit.symbol());
  }

  public int eventsPerYear() {
    return this.eventsPerYear;
  }

  public double eventsPerYearEstimate() {
    return this.eventsPerYearEstimate;
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
}
