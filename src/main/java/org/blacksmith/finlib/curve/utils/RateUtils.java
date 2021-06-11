package org.blacksmith.finlib.curve.utils;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;

public class RateUtils {
  /* Continuous Compound*/
  public static double interestRate100ToDcfContDisc(LocalDate asOfDate, LocalDate dcfDate, double rate100, double yearLength) {
    return interestRateToDcfContDisc(asOfDate, dcfDate, rate100 / 100d, yearLength);
  }

  /**
   * PV = FV / (e^(r*t)) = FV * DCF
   * FV = PV * e^(r*t) = PV / DCF
   * DCF = 1 / (e^(r*t)) = e^(-rate*t)
   */
  public static double interestRateToDcfContDisc(LocalDate asOfDate, LocalDate dcfDate, double r, double yearLength) {
    double years = DateUtils.yearsFractionalBetween(asOfDate, dcfDate, yearLength);
    return Math.exp(-r * years);
  }

  /**
   * XNPV
   * PV = FV / ((1 + r)^((di - d0) / 365)) = FV * DCF
   * DCF = 1 / ((1 + r)^((di - d0) / 365)) = (1 + r)^(-(di - d0) / 365)
   */
  public static double interestRateToDcf(LocalDate asOfDate, LocalDate dcfDate, double r, double yearLength) {
    double years = DateUtils.yearsFractionalBetween(asOfDate, dcfDate, yearLength);
    return Math.pow(1 + r, - years);
  }

  /** Regular interest rates
   * m - payments per year
   * n - number of payment
   * rate - rate
   * */
  public static double interestRateToDcf(int m, int n, double r) {
    return Math.pow(1 + (r / m), -m * n);
  }

  public static double discountFactor(double interestRate, double years) {
    return Math.exp(-interestRate * years);
  }

  public static double calculateFra(LocalDate asOfDate, LocalDate d1, LocalDate d2, double dcf1, double dcf2, double yearLength) {
    long l1 = DateUtils.daysBetween(d1, asOfDate);
    long l2 = DateUtils.daysBetween(d2, asOfDate);
    if (dcf2 == 0)
      return 0d;
    return (dcf1 / dcf2) * (yearLength / (l2 - l1));
  }
}
