package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;

public interface InterestBasis {
  /**
   * Gets the year fraction between the specified dates (period).
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention. The dates must be in order.
   *
   * @param startDate  the period start date
   * @param endDate    the period end date
   * @param scheduleInfo  the schedule information
   * @return the year fraction, zero or greater
   * @throws IllegalArgumentException if the dates are not in order
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);

  /**
   * Gets the relative year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * The result of this method will be negative if the first date is after the second date.
   * The result is calculated using {@link #yearFraction(LocalDate, LocalDate, ScheduleParameters)}.
   * <p>
   * This uses a simple {@link ScheduleParameters} which has the end-of-month convention
   * set to true, but throws an exception for other methods.
   * Certain implementations of {@code DayCount} need the missing information,
   * and thus will throw an exception.
   *
   * @param startDate  the coupon start date
   * @param calcDate   the fraction calculation date
   * @param endDate    the coupon end date
   * @param scheduleInfo  the schedule information
   * @return the year fraction, may be negative
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
//  default double relativeYearFraction(LocalDate firstDate, LocalDate secondDate) {
//    return relativeYearFraction(firstDate, secondDate, ScheduleParameters.SIMPLE_SCHEDULE_PARAMETERS);
//  }

  /**
   * Gets the relative year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * The result of this method will be negative if the first date is after the second date.
   * The result is calculated using {@link #yearFraction(LocalDate, LocalDate, LocalDate, ScheduleParameters)}.
   *
   * @param startDate  the coupon start date
   * @param calcDate   the fraction calculation date 
   * @param endDate    the coupon end date
   * @param scheduleInfo  the schedule information
   * @return the year fraction, may be negative
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
//  default double relativeYearFraction(LocalDate startDate, LocalDate endDate, LocalDate calcDate, ScheduleParameters scheduleInfo) {
//    if (endDate.isBefore(startDate)) {
//      return -yearFraction(endDate, startDate, calcDate, scheduleInfo);
//    }
//    return yearFraction(startDate, endDate, calcDate, scheduleInfo);
//  }

  /**
   * Calculates the number of days between the specified dates using the rules of this day count.
   * <p>
   * A day count is typically defines as a count of days divided by a year estimate.
   * This method returns the count of days, which is the numerator of the division.
   * For example, the 'Act/Act' day count will return the actual number of days between
   * the two dates, but the '30/360 ISDA' will return a value based on 30 day months.
   *
   * @param startDate  the start date
   * @param endDate  the sendd date, which may not be before the start date
   * @return the number of days, as determined by the day count
   */
  int days(LocalDate startDate, LocalDate endDate);
}
