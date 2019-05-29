package org.blacksmith.finlib.basic;

import java.time.LocalDate;
import java.time.Period;

import org.blacksmith.commons.date.LocalDateUtils;


import static java.lang.Math.toIntExact;
import static org.blacksmith.commons.date.LocalDateUtils.daysBetween;
import static org.blacksmith.commons.date.LocalDateUtils.nextLeapDay;

//TR:A,B,C,D,E,F,G,J,K,L,N
public enum StandardInterestBasis implements  InterestBasis {

  /**
   * Always one
   */
  ONE_ONE("1/1") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return 1;
    }
    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return 1;
    }
  },

  /**
   * Name:  ACT/360
   * Summary: Divides the actual number of days by 360
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 360.
   * Also known: 'French'
   * Definition: 2006 ISDA definitions 4.16e and ICMA rule 251.1(i) part 1
   * TR: E=ACT/360
   * */
  ACT_360("ACT/360") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return daysBetween(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },

  /**
   * Name ACT/364
   * Summary: Divides the actual number of days by 364
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 364.
   * */
  // simple actual days / 364
  ACT_364("ACT/364") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return daysBetween(startDate, endDate) / 364d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },

  /**
   * Name:  ACT/365
   * Summary: Divides the actual number of days by 365
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 360.
   * Also known: 'English'
   * Definition: 2006 ISDA definitions 4.16d
   * TR: F=ACT/365
   * */
  ACT_365("ACT/365") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return daysBetween(startDate, endDate) / 365d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },

  /**
   * Name:  ACT/365.25
   * Summary: Divides the actual number of days by 365.25
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 365.25.
   * Also known:
   * Definition:
   * */
  ACT_365_25("ACT/365.25") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return daysBetween(startDate, endDate) / 365.25d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      long actualDays = daysBetween(startDate, endDate);
      return toIntExact(actualDays);
    }
  },

  /**
   * Name: ACT/365 ACT
   * Summary: Divides the actual number of days by 366 if a leap day is contained, or by 365 if not
   * Description:
   *  The result is calculated in two parts.
   *  The actual number of days in the requested period that fall in a leap year is divided by 366.
   *  The actual number of days in the requested period that fall in a standard year is divided by 365.
   *  The result is the sum of the two.
   * Also known: 'Act/365A'
   * Definition: 2006 ISDA definitions 4.16b
   */
  ACT_365_ACT("ACT/365 ACT") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      long days = daysBetween(startDate, endDate);
      double denominator = (nextLeapDay(startDate).isAfter(endDate) ? 365d : 366d);
      return days / denominator;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },

  /**
   * Name: ACT/ACT ISDA
   * Summary: Divides the actual number of days in a leap year by 366 and the actual number of days in a standard year by 365
   * Description:
   *  The result is calculated in two parts.
   *  The actual number of days in the requested period that fall in a leap year is divided by 366.
   *  The actual number of days in the requested period that fall in a standard year is divided by 365.
   *  The result is the sum of the two.
   * Definition: 2006 ISDA definitions 4.16b
   * TR: J=ACT/ACT (ISDA)
   */
  ACT_ACT_ISDA("ACT/ACT ISDA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      int y1 = startDate.getYear();
      int y2 = endDate.getYear();
      double firstYearLength = startDate.lengthOfYear();
      if (y1 == y2) {
        double actualDays = endDate.getDayOfYear() - startDate.getDayOfYear();
        return actualDays / firstYearLength;
      }
      else {
        double firstYearDays = firstYearLength - startDate.getDayOfYear() + 1;
        double lastYearDays = endDate.getDayOfYear() - 1;
        double lastYearLength = endDate.lengthOfYear();
        return firstYearDays / firstYearLength +
            lastYearDays / lastYearLength +
            (y2 - y1 - 1);
      }
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },

  // complex ICMA calculation
  ACT_ACT_ICMA("ACT/ACT ICMA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      // avoid using ScheduleInfo in this case
      if (startDate.equals(endDate)) {
        return 0d;
      }
      // calculation is based on the schedule period, firstDate assumed to be the start of the period
      LocalDate scheduleStartDate = scheduleInfo.getStartDate();
      LocalDate scheduleEndDate = scheduleInfo.getEndDate();
      LocalDate nextCouponDate = scheduleInfo.getPeriodEndDate(startDate);
      Frequency freq = scheduleInfo.getFrequency();
      boolean eom = scheduleInfo.isEndOfMonthConvention();
      // final period, also handling single period schedules
      if (nextCouponDate.equals(scheduleEndDate)) {
        return finalPeriod(startDate, endDate, freq, eom);
      }
      // initial period
      if (startDate.equals(scheduleStartDate)) {
        return initPeriod(startDate, endDate, nextCouponDate, freq, eom);
      }
      double actualDays = daysBetween(startDate, endDate);
      double periodDays = daysBetween(startDate, nextCouponDate);
      return actualDays / (freq.eventsPerYear() * periodDays);
    }

    // calculate nominal periods backwards from couponDate
    private double initPeriod(LocalDate startDate, LocalDate endDate, LocalDate couponDate, Frequency freq, boolean eom) {
      LocalDate currentNominal = couponDate;
      LocalDate prevNominal = eom(couponDate, currentNominal.minus(freq), eom);
      double result = 0;
      while (prevNominal.isAfter(startDate)) {
        result += calc(prevNominal, currentNominal, startDate, endDate, freq);
        currentNominal = prevNominal;
        prevNominal = eom(couponDate, currentNominal.minus(freq), eom);
      }
      return result + calc(prevNominal, currentNominal, startDate, endDate, freq);
    }

    // calculate nominal periods forwards from couponDate
    private double finalPeriod(LocalDate couponDate, LocalDate endDate, Frequency freq, boolean eom) {
      LocalDate curNominal = couponDate;
      LocalDate nextNominal = eom(couponDate, curNominal.plus(freq), eom);
      double result = 0;
      while (nextNominal.isBefore(endDate)) {
        result += calc(curNominal, nextNominal, curNominal, endDate, freq);
        curNominal = nextNominal;
        nextNominal = eom(couponDate, curNominal.plus(freq), eom);
      }
      return result + calc(curNominal, nextNominal, curNominal, endDate, freq);
    }

    // apply eom convention
    private LocalDate eom(LocalDate base, LocalDate calc, boolean eom) {
      return (eom && base.getDayOfMonth() == base.lengthOfMonth() ? calc.withDayOfMonth(calc.lengthOfMonth()) : calc);
    }

    // calculate the result
    private double calc(LocalDate prevNominal, LocalDate curNominal, LocalDate start, LocalDate end, Frequency freq) {
      if (end.isAfter(prevNominal)) {
        long curNominalEpochDay = curNominal.toEpochDay();
        long prevNominalEpochDay = prevNominal.toEpochDay();
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        double periodDays = curNominalEpochDay - prevNominalEpochDay;
        double actualDays = Math.min(endEpochDay, curNominalEpochDay) - Math.max(startEpochDay, prevNominalEpochDay);
        return actualDays / (freq.eventsPerYear() * periodDays);
      }
      return 0;
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate secondDate) {
      long actualDays = daysBetween(firstDate, secondDate);
      return toIntExact(actualDays);
    }
  },

  /**
   * Name: ACT/ACT AFB
   * Summary:	Divides the actual number of days by 366 if a leap day is contained, or by 365 if not, with additional rules for periods over one year
   * Description: The result is a simple division.
   *   The numerator is the actual number of days in the requested period.
   *   The denominator is determined by examining the period end date (the date of the next coupon).
   *   The denominator is 366 if the schedule period contains February 29th, if not it is 365.
   *   The first day in the schedule period is included, the last day is excluded.
   *   Read the Javadoc for a discussion of the algorithm, the original French text and confusion with the ISDA clarification.
   * Definition: Association Francaise des Banques in September 1994 as 'Base Exact/Exact' in 'Definitions Communes plusieurs Additifs Techniques'
   * Strata: ACT_ACT_AFB
   * TR: G=ACT/ACT
   */
  ACT_ACT_AFB("ACT/ACT AFB") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      final int years = Period.between(startDate, endDate).getYears();
      final LocalDate remainedPeriodEndDate = (years==0) ? endDate: endDate.minusYears(years);
      final long remainedPeriodDays = daysBetween(startDate, remainedPeriodEndDate);
      final double remainedPeriodYearLength = LocalDateUtils.isLeapDayInPeriod(startDate,remainedPeriodEndDate) ? 366d : 365d;
      return years + (remainedPeriodDays / remainedPeriodYearLength);
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate secondDate) {
      return toIntExact(daysBetween(firstDate, secondDate));
    }
  },

  /**
   * Name: ACT/ACT YEAR
   * Summary: Divides the actual number of days by the actual number of days in the year from the start date
   * Description:  The result is calculated in two parts - a number of whole years and the remaining part.
   *  If the period is over one year, a number of years is added to the start date to reduce the remaining period to less than a year. If the start date is February 29th, then each time a year is added the last valid day in February is chosen.
   *  The remaining period is then processed by a simple division.
   *  The numerator is the actual number of days in the remaining period.
   *  The denominator is the actual number of days in the year from the adjusted start date.
   *  The first day in the period is included, the last day is excluded.
   *  The result is the number of whole years plus the result of the division.
   * Strata: ACT_ACT_YEAR
   */
  ACT_ACT_YEAR("ACT/ACT YEAR") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      final int years = Period.between(startDate, endDate).getYears();
      final LocalDate remainedPeriodStartDate = years==0 ? startDate : startDate.plusYears(years);
      final double remainedPeriodDays = daysBetween(remainedPeriodStartDate, endDate);
      final double remainedPeriodYearLength = daysBetween(remainedPeriodStartDate, remainedPeriodStartDate.plusYears(1));
      return years + remainedPeriodDays/remainedPeriodYearLength;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return toIntExact(daysBetween(startDate, endDate));
    }
  },


  // actual days / 365 or 366
  ACT_365L("Act/365L") {
    @Override
    public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
      long actualDays = daysBetween(firstDate, secondDate);
      // calculation is based on the end of the schedule period (next coupon date) and annual/non-annual frequency
      LocalDate nextCouponDate = scheduleInfo.getPeriodEndDate(firstDate);
      if (scheduleInfo.getFrequency().isAnnual()) {
        LocalDate nextLeap = nextLeapDay(firstDate);
        return actualDays / (nextLeap.isAfter(nextCouponDate) ? 365d : 366d);
      } else {
        return actualDays / (nextCouponDate.isLeapYear() ? 366d : 365d);
      }
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate secondDate) {
      return toIntExact(daysBetween(firstDate, secondDate));
    }
  },

  /**
   * Name: NL/360
   * Summary: Divides the actual number of days omitting leap days by 360
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period minus the number of occurrences of February 29.
   *  The denominator is always 365.
   *  The first day in the period is excluded, the last day is included.
   * Also known: 'ACT/360 No Leap'
   * TR: C=365/360
   */
  NL_360("NL/360") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysNL(startDate,endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysNL(startDate,endDate);
    }
  },

  /**
   * Name: NL/365
   * Summary: Divides the actual number of days omitting leap days by 365
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period minus the number of occurrences of February 29.
   *  The denominator is always 365.
   *  The first day in the period is excluded, the last day is included.
   * Also known: 'ACT/365 No Leap'
   * TR: D=365/365
   */
  NL_365("NL/365") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysNL(startDate,endDate) / 365d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysNL(startDate,endDate);
    }
  },

  /**
   * Name: D30_360_ISDA
   * Summary: A 30/360 style algorithm with special rules for the 31st day-of-month
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay is then calculated once day-of-month adjustments have occurred.
   *  If the second day-of-month is 31 and the first day-of-month is 30 or 31, change the second day-of-month to 30.
   *  If the first day-of-month is 31, change the first day-of-month to 30.
   * Also known: '30/360 U.S. Municipal' or '30/360 Bond Basis'
   * Definition: 2006 ISDA definitions 4.16f.
   * Strata: THIRTY_360_ISDA
   * TR: L=30/360 ISDA
   */
  D30_360_ISDA("30/360 ISDA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30ISDA(startDate,endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30ISDA(startDate,endDate);
    }
  },

  /**
   * Name: D30_E_360_ISDA
   * Summary:	A 30/360 style algorithm with special rules for the 31st day-of-month and the end of February
   * Description 	The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *   The deltaDay is then calculated once day-of-month adjustments have occurred.
   *   If the first day-of-month is 31, change the first day-of-month to 30.
   *   If the second day-of-month is 31, change the second day-of-month to 30.
   *   If the first date is the last day of February, change the first day-of-month to 30.
   *   If the second date is the last day of February and it is not the maturity date, change the second day-of-month to 30.
   *   Schedules 	This day count requires ScheduleInfo
   * Also known: '30E/360 German' or 'German'
   * Definition: 2006 ISDA definitions 4.16h
   * Strata: THIRTY_E_360_ISDA
   * TR:
   */
  //TODO: daysBetween30EISDA/360D;
  D30_E_360_ISDA("30E/360 ISDA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31 || LocalDateUtils.isLastDayOfFebruary(startDate)) {
        date1.setDay(30);
      }
      if (date2.getDay()==31 || (LocalDateUtils.isLastDayOfFebruary(endDate) && !endDate.equals(scheduleInfo.getEndDate()))) {
        date2.setDay(30);
      }
      return DayCountUtils.days360(date1,date2) /360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30EISDA(startDate,endDate);
    }
  },
  // US thirty day months / 360 with dynamic EOM rule
  XXX_THIRTY_U_360("30U/360") {
    @Override
    public double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
      if (scheduleInfo.isEndOfMonthConvention()) {
        return D30_U_360_EOM.calculateYearFraction(firstDate, secondDate, scheduleInfo);
      } else {
        return D30_360_ISDA.calculateYearFraction(firstDate, secondDate, scheduleInfo);
      }
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate secondDate) {
      return D30_360_ISDA.days(firstDate, secondDate);
    }
  },

  /**
   * Name: "30U/360 EOM
   * Summary: A 30/360 style algorithm with special rules for the 31st day-of-month and the end of February
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay is then calculated once day-of-month adjustments have occurred.
   *  If both dates are the last day of February, change the second day-of-month to 30.
   *  If the first date is the last day of February, change the first day-of-month to 30.
   *  If the second day-of-month is 31 and the first day-of-month is 30 or 31, change the second day-of-month to 30.
   *  If the first day-of-month is 31, change the first day-of-month to 30.
   *  This day count is not dependent on the EOM flag in ScheduleInfo.
   *  This is the same as '30U/360' when the EOM convention applies.
   *  This day count would typically be used to be explicit about the EOM rule applying.
   *  In most cases, '30U/360' should be used in preference to this day count.
   * Also known: '30/360 US', '30US/360' or '30/360 SIA'
   * Strata: THIRTY_U_360_EOM
   */

  // US thirty day months / 360 with fixed EOM rule
  D30_U_360_EOM("30U/360 EOM") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30USEOM(startDate,endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30USEOM(startDate,endDate);
    }
  },

  /**
   * Name: 30/360 PSA
   * Summary: A 30/360 style algorithm with special rules for the 31st day-of-month and the end of February
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay is then calculated once day-of-month adjustments have occurred.
   *  If the startDate day-of-month is 31 or the last day of February, change the startDate day-of-month to 30.
   *  If the endDate day-of-month is 31 and the startDate day-of-month is 30 or 31 or the last day of February, change the endDate day-of-month to 30.
   * Schedules 	This day count assumes EOM convention is true if ScheduleInfo is not specified
   * Also known: '30/360 BMA' (PSA is the Public Securites Association, BMA is the Bond Market Association)
   * Strata: THIRTY_360_PSA
   * TR: N=30G/360 BMA
   */
  D30_360_PSA("30/360 PSA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30PSA(startDate,endDate)/360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30PSA(startDate,endDate);
    }
  },

  /**
   * Name: 30E/360
   * Summary: A 30/360 style algorithm with special rules for the 31st day-of-month
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay is then calculated once day-of-month adjustments have occurred.
   *  If the startDate day-of-month is 31, it is changed to 30.
   *  If the endDate day-of-month is 31, it is changed to 30.
   * Also known: '30/360 ISMA', '30/360 European', '30S/360 Special German' or 'Eurobond'
   * Definition: 2006 ISDA definitions 4.16g and ICMA rule 251.1(ii) and 252.2
   * Strata: THIRTY_E_360
   * TR: A=30/360
   */
  D30_E_360("30E/360") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30E(startDate,endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30E(startDate, endDate);
    }
  },

  /**
   * Name: 30E/365
   * Summary: A 30/365 style algorithm with special rules for the 31st day-of-month
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay is then calculated once day-of-month adjustments have occurred.
   *  If the startDate day-of-month is 31, it is changed to 30.
   *  If the endDate day-of-month is 31, it is changed to 30.
   * Also known:
   * Definition:
   * TR: B=30/365
   */
  D30_E_365("30E/365") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30E(startDate,endDate) / 365d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30E(startDate, endDate);
    }
  },

  /**
   * Name: 30E+/360
   * Summary: A 30/360 style algorithm with special rules for the 31st day-of-month
   * Description:
   *  The result is calculated as (360 * deltaYear + 30 * deltaMonth + deltaDay) / 360.
   *  The deltaDay and deltaMonth are calculated once adjustments have occurred.
   *  If the first day-of-month is 31, it is changed to 30.
   *  If the second day-of-month is 31, it is changed to 1 and the second month is incremented.
   * Strata: THIRTY_EPLUS_360
   * TR: K=30E+/360
   */
  D30_EPLUS_360("30E+/360") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return DayCountUtils.daysBetween30EPlus(startDate, endDate)/360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30EPlus(startDate,endDate);
    }
  };

  // calculate using the standard 30/360 function - 360(y2 - y1) + 30(m2 - m1) + (d2 - d1)) / 360
  private static double thirty360x(int y1, int m1, int d1, int y2, int m2, int d2) {
    return (360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1)) / 360d;
  }

  //calculate using the 30/360 function as above but does not divide by 360, as the number of days is needed, not the fraction.
  private static int thirty360Days(int y1, int m1, int d1, int y2, int m2, int d2) {
    return 360 * (y2 - y1) + 30 * (m2 - m1) + (d2 - d1);
  }

  @Override
  public double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Dates must be in time-line order");
    }
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateYearFraction(startDate, endDate, scheduleInfo);
  }

  //@Override
  public int days(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Dates must be in time-line order");
    }
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateDays(startDate, endDate);
  }

  @Override
  public double relativeYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    // override to avoid duplicate null checks
    if (secondDate.isBefore(firstDate)) {
      return -yearFraction(secondDate, firstDate, scheduleInfo);
    }
    return yearFraction(firstDate, secondDate, scheduleInfo);
  }

  // calculate the year fraction, using validated inputs
  abstract double calculateYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo);

  //calculate the number of days between the specified dates, using validated inputs
  abstract int calculateDays(LocalDate firstDate, LocalDate secondDate);

  String shortName;

  StandardInterestBasis(String shortName) {
    this.shortName = shortName;
  }
  public String getShortName() {
    return this.shortName;
  }
}
