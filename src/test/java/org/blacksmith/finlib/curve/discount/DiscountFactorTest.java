package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountFactorTest {
  final LocalDate date = LocalDate.of(2021, 5, 31);
  @Test
  public void zeroDayDcf() {
    DiscountFactor discountFactor = ZeroRateDiscountFactor.of();
    assertThat(discountFactor.discountFactor(0d, 0d)).isEqualTo(1.0);
    assertThat(discountFactor.discountFactor(0d, 2d)).isEqualTo(1.0);
  }

  @Test
  public void oneYearDcf() {
    LocalDate secondDate = date.plusYears(1);
    DayCount dayCount = StandardDayCounts.ACT_365;
    DiscountFactor discountFactor = ZeroRateDiscountFactor.of();
    assertThat(discountFactor.discountFactor(dayCount.yearFraction(date, secondDate), 0.1d)).isEqualTo(0.9048374180359595d);//.isEqualTo(RateUtils.interestRateToDcfContDisc(date, nextYear, 0.1d, 365));
  }

  @Test
  public void oneYearDcf1() {
    LocalDate secondDate = date.plusYears(1);
    DayCount dayCount = StandardDayCounts.ACT_365_2425;
    DiscountFactor discountFactor = ZeroRateDiscountFactor.of();
    assertThat(discountFactor.discountFactor(dayCount.yearFraction(date, secondDate), 0.1d)).isEqualTo(0.9048974960274994);
  }
}
