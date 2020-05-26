package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.dayconvention.utils.DayCountUtils;
import org.blacksmith.finlib.dayconvention.utils.YMD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Math.toIntExact;
import static org.blacksmith.commons.datetime.DateUtils.daysBetween;
import static org.blacksmith.commons.datetime.DateUtils.yearsBetween;
import static org.blacksmith.commons.datetime.DateUtils.isLastDayOfFebruary;
import static org.blacksmith.commons.datetime.DateUtils.isLeapDayInPeriod;
import static org.blacksmith.commons.datetime.DateUtils.nextLeapDay;

//TR:A,B,C,D,E,F,G,J,K,L,N
public enum StandardDayCountConvention implements DayCountConvention {

  /**
   * Always one
   */
  ONE_ONE("1/1") {
    @Override
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return 1d;
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate);
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
  ACT_364("ACT/364") {
    @Override
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate) / 365.25d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      int days = daysBetween(startDate, endDate);
      //end date included
      double denominator = isLeapDayInPeriod(startDate,endDate) ? 366d : 365d;
      return days / denominator;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      int y1 = startDate.getYear();
      int y2 = endDate.getYear();
      int firstYearLength = startDate.lengthOfYear();
      if (y1 == y2) {
        int actualDays = endDate.getDayOfYear() - startDate.getDayOfYear();
//        LOGGER.debug("ACT_ACT_ISDA actualDays={} firstYearLength={}",actualDays,firstYearLength);
        return (double)actualDays / firstYearLength;
      }
      else {
        int firstRemainderOfYear = firstYearLength - startDate.getDayOfYear() + 1;
        int secondRemainderOfYear = endDate.getDayOfYear() - 1;
        int secondYearLength = endDate.lengthOfYear();
//        LOGGER.debug("ACT_ACT_ISDA firstRemainderOfYear={} firstYearLength={} secondRemainderOfYear={} secondYearLength={}",
//            firstRemainderOfYear,firstYearLength,secondRemainderOfYear,secondYearLength);
        return (double)firstRemainderOfYear / firstYearLength +
            (double)secondRemainderOfYear / secondYearLength +
            (y2 - y1 - 1);
      }
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return daysBetween(startDate, endDate);
    }
  },

  // complex ICMA calculation
  ACT_ACT_ISMA("ACT/ACT ISMA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      // calculation is based on the schedule period, firstDate assumed to be the start of the period
      LocalDate scheduleStartDate = scheduleInfo.getStartDate();
      LocalDate scheduleEndDate = scheduleInfo.getMaturityDate();
      LocalDate nextCouponDate = scheduleInfo.getCouponEndDate();
      Frequency freq = scheduleInfo.getCouponFrequency();
      boolean eom = scheduleInfo.isEndOfMonthConvention();

      // final period, also handling single period schedules
      if (nextCouponDate.equals(scheduleEndDate)) {
        LocalDate endCalculated = eom(startDate, freq.addTo(startDate), eom);
        if (endCalculated.isBefore(scheduleEndDate))
          return initPeriod(startDate, endDate, nextCouponDate, freq, eom);
        else
          return finalPeriod(startDate, endDate, freq, eom);
      }
      // initial period
      else if (scheduleStartDate.equals(startDate)) {
        return initPeriod(startDate, endDate, nextCouponDate, freq, eom);
      }
      else
        return otherPeriod(startDate,endDate,nextCouponDate,freq);
    }
    
    // calculate nominal periods backwards from couponDate
    private double initPeriod(LocalDate startDate, LocalDate endDate, LocalDate couponDate, Frequency freq, boolean eom) {
      LocalDate subPeriodEnd = couponDate;
      LocalDate subPeriodStart = eom(couponDate, freq.minusFrom(subPeriodEnd), eom);
//      LOGGER.debug("initPeriod subStart={} subEnd={} startDate={} endDate{}",subPeriodStart,subPeriodEnd,startDate,endDate);
      double result = 0;
      while (subPeriodStart.isAfter(startDate)) {
        result += calc(subPeriodStart, subPeriodEnd, startDate, endDate, freq);
        subPeriodEnd = subPeriodStart;
        subPeriodStart = eom(couponDate, freq.minusFrom(subPeriodEnd), eom);
//        LOGGER.debug("from final periodStart={} periodEnd={} endDate{}",subPeriodStart,subPeriodEnd,endDate);
      }
      return result + calc(subPeriodStart, subPeriodEnd, startDate, endDate, freq);
    }

    // calculate nominal periods forwards from couponDate
    private double finalPeriod(LocalDate couponDate, LocalDate endDate, Frequency freq, boolean eom) {
      LocalDate subPeriodStart = couponDate;
      LocalDate subPeriodEnd = eom(couponDate, freq.addTo(subPeriodStart), eom);
//      LOGGER.debug("finalPeriod subStart={} subEnd={} endDate{}",subPeriodStart,subPeriodEnd,endDate);
      double result = 0;
      while (subPeriodEnd.isBefore(endDate)) {
        result += calc(subPeriodStart, subPeriodEnd, subPeriodStart, endDate, freq);
        subPeriodStart = subPeriodEnd;
        subPeriodEnd = eom(couponDate, freq.addTo(subPeriodEnd), eom);
      }
      return result + calc(subPeriodStart, subPeriodEnd, subPeriodStart, endDate, freq);
    }
    
    private double otherPeriod(LocalDate startDate, LocalDate endDate, LocalDate couponEndDate, Frequency freq) {
      int actualDays = daysBetween(startDate, endDate);
      int periodDays = daysBetween(startDate, couponEndDate);
      return (double)actualDays / (freq.eventsPerYear() * periodDays);      
    }


    // apply eom convention
    private LocalDate eom(LocalDate base, LocalDate calc, boolean eom) {
      return (eom && base.getDayOfMonth() == base.lengthOfMonth() ? calc.withDayOfMonth(calc.lengthOfMonth()) : calc);
    }

    // calculate the result
    private double calc(LocalDate prevNominal, LocalDate curNominal, LocalDate start, LocalDate end, Frequency freq) {
//      LOGGER.debug("calc prev={} end={}",prevNominal,end);
      if (end.isAfter(prevNominal)) {
        long curNominalEpochDay = curNominal.toEpochDay();
        long prevNominalEpochDay = prevNominal.toEpochDay();
        long startEpochDay = start.toEpochDay();
        long endEpochDay = end.toEpochDay();
        double periodDays = (double)curNominalEpochDay - prevNominalEpochDay;
        double actualDays = (double)Math.min(endEpochDay, curNominalEpochDay) - Math.max(startEpochDay, prevNominalEpochDay);
//        LOGGER.debug("calc curNominalEpochDay={} prevNominalEpochDay={} actualDays={} periodDays={} freqPerYear={}",curNominalEpochDay,prevNominalEpochDay,actualDays,
//            periodDays, freq.eventsPerYear());
        return actualDays / (freq.eventsPerYear() * periodDays);
      }
      return 0;
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate secondDate) {
      return daysBetween(firstDate, secondDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      final int years = yearsBetween(startDate, endDate);
      final LocalDate remainedPeriodEndDate = (years==0) ? endDate: endDate.minusYears(years);
      final int remainedPeriodDays = daysBetween(startDate, remainedPeriodEndDate);
      final double remainedPeriodYearLength = isLeapDayInPeriod(startDate,remainedPeriodEndDate) ? 366d : 365d;
//      LOGGER.debug("ACT_ACT_AFB years={} remainedPeriodDays={} remainedPeriodYearLength={}",years,remainedPeriodDays,remainedPeriodYearLength);
      return years + (remainedPeriodDays / remainedPeriodYearLength);
    }

    @Override
    public int calculateDays(LocalDate firstDate, LocalDate calcDate) {
      return daysBetween(firstDate, calcDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      final int years = yearsBetween(startDate, endDate);
      final LocalDate remainedPeriodStartDate = years==0 ? startDate : startDate.plusYears(years);
      final int remainedPeriodDays = daysBetween(remainedPeriodStartDate, endDate);
      final double remainedPeriodYearLength = daysBetween(remainedPeriodStartDate, remainedPeriodStartDate.plusYears(1));
      return years + remainedPeriodDays/remainedPeriodYearLength;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate calcDate) {
      return daysBetween(startDate, calcDate);
    }
  },


  // actual days / 365 or 366
  //TODO CHECK
  ACT_365L("Act/365L") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      int actualDays = daysBetween(startDate, endDate);
      // calculation is based on the end of the schedule period (next coupon date) and annual/non-annual frequency
      LocalDate nextCouponDate = scheduleInfo.getCouponEndDate();
      if (scheduleInfo.getCouponFrequency().isAnnual()) {
        LocalDate nextLeap = nextLeapDay(startDate);
        return actualDays / (nextLeap.isAfter(nextCouponDate) ? 365d : 366d);
      } else {
        return actualDays / (nextCouponDate.isLeapYear() ? 366d : 365d);
      }
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate calcDate) {
      return daysBetween(startDate, calcDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.days365(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.days365(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.days365(startDate, endDate) / 365d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.days365(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30ISDA(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30ISDA(startDate, endDate);
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
  D30_E_360_ISDA("30E/360 ISDA") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      YMD date1 = YMD.of(startDate);
      YMD date2 = YMD.of(endDate);
      if (date1.getDay()==31 || isLastDayOfFebruary(startDate)) {
        date1.setDay(30);
      }
      if (date2.getDay()==31 || (isLastDayOfFebruary(endDate) && !endDate.equals(scheduleInfo.getMaturityDate()))) {
        date2.setDay(30);
      }
      return DayCountUtils.days360(date1,date2) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate calcDate) {
      return DayCountUtils.daysBetween30EISDA(startDate,calcDate);
    }
  },
  // US thirty day months / 360 with dynamic EOM rule
  D30_U_360("30U/360") {
    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      if (scheduleInfo.isEndOfMonthConvention()) {
        return D30_U_360_EOM.calculateYearFraction(startDate, endDate, scheduleInfo);
      } else {
        return D30_360_ISDA.calculateYearFraction(startDate, endDate, scheduleInfo);
      }
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate calcDate) {
      return D30_360_ISDA.days(startDate, calcDate);
    }
  },

  /**
   * Name: 30U/360 EOM
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30USEOM(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30USEOM(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30PSA(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30PSA(startDate, endDate);
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30E(startDate, endDate) / 360d;
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30E(startDate, endDate) / 365d;
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
    public double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30EPlus(startDate, endDate) / 360d;
    }

    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate) {
      return DayCountUtils.daysBetween30EPlus(startDate,endDate);
    }
  };

  @Override
  public double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Dates must be in time-line order");
    }
    if (scheduleInfo!=null) {
      if (startDate.isBefore(scheduleInfo.getStartDate())) {
        throw new IllegalArgumentException("Dates must be in time-line order");
      }
      if (endDate.isAfter(scheduleInfo.getMaturityDate())) {
        throw new IllegalArgumentException("Dates must be in time-line order");
      }
    }
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateYearFraction(startDate, endDate, scheduleInfo);
  }

  @Override
  public int days(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("Dates must be in time-line order");
    }
    if (endDate.isEqual(startDate)) {
      return 0;
    }
    return calculateDays(startDate, endDate);
  }

  // calculate the year fraction, using validated inputs
  public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return calculateYearFractionSimple(startDate, endDate);
  }

  protected double calculateYearFractionSimple(LocalDate startDate, LocalDate endDate) {
      return 0d;
  }
    
  //calculate the number of days between the specified dates, using validated inputs
  abstract int calculateDays(LocalDate startDate, LocalDate endDate);

  final String shortName;

  StandardDayCountConvention(String shortName) {
    this.shortName = shortName;
  }
  public String getShortName() {
    return this.shortName;
  }
  private static final Logger LOGGER = LoggerFactory.getLogger(StandardDayCountConvention.class);
}
