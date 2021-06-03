package org.blacksmith.finlib.curve.utils;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RateUtilsTest {
  LocalDate date = LocalDate.of(2021, 5, 31);
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
    assertThat(0.9048374180359595d).isEqualTo(RateUtils.interestRateToDcf(date, nextYear, 0.1d, 365));
  }

}
