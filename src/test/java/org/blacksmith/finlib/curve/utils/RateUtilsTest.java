package org.blacksmith.finlib.curve.utils;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateUtilsTest {
  final LocalDate date = LocalDate.of(2021, 5, 31);
  @Test
  public void zeroDayDcf() {
    assertThat(1.0d).isEqualTo(RateUtils.interestRateToDcf(date, date, 0.0d, 365));
    assertThat(1.0d).isEqualTo(RateUtils.interestRateToDcf(date, date, 0.1d, 365));
    assertThat(1.0d).isEqualTo(RateUtils.interestRateToDcf(date, date, 1.0d, 365));
    assertThat(1.0d).isEqualTo(RateUtils.interestRateToDcf(date, date, 2.0d, 365));
    assertThat(1.0d).isEqualTo(RateUtils.interestRateToDcf(date, date, 1.0d, 365));
  }

  @Test
  public void oneYearDcf() {
    LocalDate nextYear = date.plusDays(365);
    assertThat(0.9048374180359595d).isEqualTo(RateUtils.interestRateToDcfContDisc(date, nextYear, 0.1d, 365));
    assertThat(0.9090909090909091d).isEqualTo(RateUtils.interestRateToDcf(date, nextYear, 0.1d, 365));
    assertThat(0.9090909090909091d).isEqualTo(RateUtils.interestRateToDcf(date, nextYear, 0.1d, 365));
    assertThat(0.9090909090909091d).isEqualTo(RateUtils.interestRateToDcf(1, 1, 0.1d));
//    assertThat(0.9090909090909091d).isEqualTo(RateUtils.interestRateToDcf(date, nextYear, 0.1d, 365));
  }

  public static double interestRateToDcf2(LocalDate asOfDate, LocalDate dcfDate, double rate, int yearLength) {
    long len = DateUtils.daysBetween(asOfDate, dcfDate);
    return 1 / Math.pow(1 + rate, len/yearLength);
  }

}
