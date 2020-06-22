package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.daycount.Act365ActConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActAct365LConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActActAfbConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActActIsdaConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActActIsmaConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActActYearConvention;
import org.blacksmith.finlib.interestbasis.daycount.ActConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30EConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30EIsdaConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30EPlusConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30EPsaConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30IsdaConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30UConvention;
import org.blacksmith.finlib.interestbasis.daycount.D30USEomConvention;
import org.blacksmith.finlib.interestbasis.daycount.NLConvention;
import org.blacksmith.finlib.interestbasis.daycount.DayCountConventionCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum StandardDayCountConvention implements DayCountConvention {

  /**
   * Always one
   */
  ONE_ONE("1/1", new DayCountConventionCalculator() {
    @Override
    public int calculateDays(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return 1;
    }

    @Override
    public double calculateYearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
      return 1d;
    }
  }),

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
  ACT_360("ACT/360", new ActConvention(360d)),

  /**
   * Name ACT/364
   * Summary: Divides the actual number of days by 364
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 364.
   * */
  ACT_364("ACT/364", new ActConvention(364d)),

  /**
   * Name:  ACT/365
   * Summary: Divides the actual number of days by 365
   * Description:
   *  The result is a simple division.
   *  The numerator is the actual number of days in the requested period.
   *  The denominator is always 360.
   * Also known: 'English', ACT/365F
   * Definition: 2006 ISDA definitions 4.16d
   * TR: F=ACT/365
   * */
  ACT_365("ACT/365", new ActConvention((365d))),

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
  ACT_365_25("ACT/365.25", new ActConvention(365.25d)),

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
  ACT_365_ACT("ACT/365 ACT", new Act365ActConvention()),

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
  ACT_ACT_ISDA("ACT/ACT ISDA", new ActActIsdaConvention()),

  // complex ICMA calculation
  ACT_ACT_ISMA("ACT/ACT ISMA",new ActActIsmaConvention()),

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
  ACT_ACT_AFB("ACT/ACT AFB", new ActActAfbConvention()),

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
  ACT_ACT_YEAR("ACT/ACT YEAR", new ActActYearConvention()),


  // actual days / 365 or 366
  //TODO CHECK
  ACT_365L("Act/365L", new ActAct365LConvention()),

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
  NL_360("NL/360", new NLConvention(360d)),

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
  NL_365("NL/365", new NLConvention(365d)),

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
  D30_360_ISDA("30/360 ISDA", new D30IsdaConvention(360d)),

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
  D30_E_360_ISDA("30E/360 ISDA", new D30EIsdaConvention(360d)),

  // US thirty day months / 360 with dynamic EOM rule
  D30U_360("30U/360", new D30UConvention(360d)),

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
  D30U_360_EOM("30U/360 EOM", new D30USEomConvention(360d)),

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
  D30_360_PSA("30/360 PSA", new D30EPsaConvention(360d)),

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
  D30E_360("30E/360", new D30EConvention(360d)),

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
  D30E_365("30E/365", new D30EConvention(365d)),

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
  D30EPLUS_360("30E+/360", new D30EPlusConvention(360d));

  @Override
  public double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return calculator.yearFraction(startDate, endDate, scheduleInfo);
  }

  @Override
  public int days(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo) {
    return calculator.days(startDate, endDate, scheduleInfo);
  }

  final String shortName;
  final DayCountConventionCalculator calculator;

  StandardDayCountConvention(String shortName, DayCountConventionCalculator calculator) {
    this.shortName = shortName;
    this.calculator = calculator;
  }
  public String getShortName() {
    return this.shortName;
  }
  private static final Logger LOGGER = LoggerFactory.getLogger(StandardDayCountConvention.class);
}
