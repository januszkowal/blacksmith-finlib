package org.blacksmith.finlib.interest;

import java.time.LocalDate;

import org.blacksmith.finlib.interest.utils.InterestCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InterestCalculatorTest {
  @Test
  public void testFactor() {
    LocalDate startDate = LocalDate.of(2020, 1, 1);
    LocalDate endDate = LocalDate.of(2020, 2, 1);
    LocalDate interestDateBefore = LocalDate.of(2019, 12, 31);
    LocalDate interestDate0 = LocalDate.of(2002, 1, 1);
    LocalDate interestDate1 = LocalDate.of(2020, 1, 2);
    LocalDate interestDate2 = LocalDate.of(2020, 1, 3);
    assertThat(InterestCalculator.getFactor(interestDateBefore, startDate, endDate)).isEqualTo(0d);
    assertThat(InterestCalculator.getFactor(interestDate0, startDate, endDate)).isEqualTo(0d);
    assertThat(InterestCalculator.getFactor(interestDate1, startDate, endDate)).isEqualTo(1d / 31);
    assertThat(InterestCalculator.getFactor(interestDate2, startDate, endDate)).isEqualTo(2d / 31);
    assertThat(InterestCalculator.getFactor(endDate, startDate, endDate)).isEqualTo(1d);
    assertThat(InterestCalculator.getFactor(endDate, startDate, endDate)).isEqualTo(1d);
  }
}
