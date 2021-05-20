package org.blacksmith.finlib.interest;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;

public class InterestCalculator {
  public static double getFactor(LocalDate interestDate, LocalDate startDate, LocalDate endDate) {
    if (interestDate.compareTo(startDate) < 0) {
      return 0d;
    } else if (interestDate.compareTo(endDate) >= 0) {
      return 1d;
    } else {
      return (double) DateUtils.daysBetween(startDate, interestDate) / DateUtils.daysBetween(startDate, endDate);
    }
  }
}
