package org.blacksmith.finlib.curve.utils;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;

public class RateUtils {
  public static double interestRate100ToDcf(LocalDate asOfDate, LocalDate dcfDate, double rate100, int yearLength) {
    return interestRateToDcf(asOfDate, dcfDate, rate100 / 100d, yearLength);
  }

  public static double interestRateToDcf(LocalDate asOfDate, LocalDate dcfDate, double rate, int yearLength) {
    long len = DateUtils.daysBetween(asOfDate, dcfDate);
    return Math.exp((-rate*len)/yearLength);
  }

  public static double getFra(LocalDate asOfDate, LocalDate d1, LocalDate d2, double dcf1, double dcf2, int yearLength) {
    long l1 = DateUtils.daysBetween(d1, asOfDate);
    long l2 = DateUtils.daysBetween(d2, asOfDate);
    if(dcf2 == 0)
      return 0d;
    return (dcf1 / dcf2) * ((double) yearLength / (l2 - l1)) * 100d;
  }
}
