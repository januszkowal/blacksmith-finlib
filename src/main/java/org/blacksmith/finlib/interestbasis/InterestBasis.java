package org.blacksmith.finlib.interestbasis;

import java.time.LocalDate;
import org.blacksmith.finlib.interestbasis.schedule.ScheduleInfo;

public interface InterestBasis {
  /**
   * Gets the year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention. The dates must be in order.
   *
   * @param startDate  the first date
   * @param endDate  the second date, on or after the first date
   * @param scheduleInfo  the schedule information
   * @return the year fraction, zero or greater
   * @throws IllegalArgumentException if the dates are not in order
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  double yearFraction(LocalDate startDate, LocalDate endDate, ScheduleInfo scheduleInfo);

  default double yearFraction(LocalDate firstDate, LocalDate secondDate) {
    return yearFraction(firstDate, secondDate, ScheduleInfo.SIMPLE_SCHEDULE_INFO);
  }
  /**
   * Gets the relative year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * The result of this method will be negative if the first date is after the second date.
   * The result is calculated using {@link #yearFraction(LocalDate, LocalDate, ScheduleInfo)}.
   * <p>
   * This uses a simple {@link ScheduleInfo} which has the end-of-month convention
   * set to true, but throws an exception for other methods.
   * Certain implementations of {@code DayCount} need the missing information,
   * and thus will throw an exception.
   *
   * @param firstDate  the first date
   * @param secondDate  the second date, which may be before the first date
   * @return the year fraction, may be negative
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  default double relativeYearFraction(LocalDate firstDate, LocalDate secondDate) {
    return relativeYearFraction(firstDate, secondDate, ScheduleInfo.SIMPLE_SCHEDULE_INFO);
  }

  /**
   * Gets the relative year fraction between the specified dates.
   * <p>
   * Given two dates, this method returns the fraction of a year between these
   * dates according to the convention.
   * The result of this method will be negative if the first date is after the second date.
   * The result is calculated using {@link #yearFraction(LocalDate, LocalDate, ScheduleInfo)}.
   *
   * @param firstDate  the first date
   * @param secondDate  the second date, which may be before the first date
   * @param scheduleInfo  the schedule information
   * @return the year fraction, may be negative
   * @throws UnsupportedOperationException if the year fraction cannot be obtained
   */
  default double relativeYearFraction(LocalDate firstDate, LocalDate secondDate, ScheduleInfo scheduleInfo) {
    if (secondDate.isBefore(firstDate)) {
      return -yearFraction(secondDate, firstDate, scheduleInfo);
    }
    return yearFraction(firstDate, secondDate, scheduleInfo);
  }

  /**
   * Calculates the number of days between the specified dates using the rules of this day count.
   * <p>
   * A day count is typically defines as a count of days divided by a year estimate.
   * This method returns the count of days, which is the numerator of the division.
   * For example, the 'Act/Act' day count will return the actual number of days between
   * the two dates, but the '30/360 ISDA' will return a value based on 30 day months.
   *
   * @param firstDate  the first date
   * @param secondDate  the second date, which may be before the first date
   * @return the number of days, as determined by the day count
   */
  int days(LocalDate firstDate, LocalDate secondDate);


}
