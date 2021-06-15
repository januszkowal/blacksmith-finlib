package org.blacksmith.finlib.valuation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.blacksmith.finlib.basic.currency.Currency;
import org.junit.jupiter.api.Test;


class CashflowAggregatorTest {

  @Test
  public void aggregateSingle() {
    LocalDate d1 = LocalDate.of(2020, 1, 15);
    List<Cashflow> cashflows = List.of(Cashflow.of(d1, BigDecimal.valueOf(22d), Currency.EUR));
    System.out.println(cashflows);
    List<Cashflow> aggregated = CashflowAggregator.aggregate(cashflows);
    Assertions.assertThat(aggregated.size()).isEqualTo(1);
  }

  @Test
  public void aggregate2() {
    LocalDate d1 = LocalDate.of(2020, 1, 15);
    LocalDate d2 = LocalDate.of(2020, 1, 20);
    List<Cashflow> cashflows = List.of(
        Cashflow.of(d1, BigDecimal.valueOf(22d), Currency.EUR),
        Cashflow.of(d1, BigDecimal.valueOf(5d), Currency.EUR)
    );
    List<Cashflow> aggregated = CashflowAggregator.aggregate(cashflows);
    System.out.println(aggregated);
    Assertions.assertThat(aggregated.size()).isEqualTo(1);
  }

  @Test
  public void aggregate3() {
    LocalDate d1 = LocalDate.of(2020, 1, 15);
    LocalDate d2 = LocalDate.of(2020, 1, 20);
    List<Cashflow> cashflows = List.of(
        Cashflow.of(d1, BigDecimal.valueOf(22d), Currency.EUR),
        Cashflow.of(d1, BigDecimal.valueOf(5d), Currency.EUR),
        Cashflow.of(d1, BigDecimal.valueOf(5d), Currency.USD),
        Cashflow.of(d1, BigDecimal.valueOf(1D), Currency.USD)
    );
    List<Cashflow> aggregated = CashflowAggregator.aggregate(cashflows);
    System.out.println(aggregated);
    Assertions.assertThat(aggregated.size()).isEqualTo(2);
  }

  @Test
  public void aggregate4() {
    LocalDate d1 = LocalDate.of(2020, 1, 15);
    LocalDate d2 = LocalDate.of(2020, 1, 20);
    List<Cashflow> cashflows = List.of(
        Cashflow.of(d1, BigDecimal.valueOf(22d), Currency.EUR),
        Cashflow.of(d1, BigDecimal.valueOf(5d), Currency.EUR),
        Cashflow.of(d2, BigDecimal.valueOf(10d), Currency.EUR),
        Cashflow.of(d2, BigDecimal.valueOf(3d), Currency.EUR),
        Cashflow.of(d1, BigDecimal.valueOf(5d), Currency.USD),
        Cashflow.of(d1, BigDecimal.valueOf(1D), Currency.USD),
        Cashflow.of(d2, BigDecimal.valueOf(3d), Currency.USD),
        Cashflow.of(d2, BigDecimal.valueOf(2D), Currency.USD)
    );
    List<Cashflow> aggregated = CashflowAggregator.aggregate(cashflows);
    System.out.println(aggregated);
    Assertions.assertThat(aggregated.size()).isEqualTo(4);
  }
}
