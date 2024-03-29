package org.blacksmith.finlib.valuation.xirr;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.valuation.dto.Cashflow;
import org.blacksmith.finlib.valuation.dto.Cashflows;
import org.blacksmith.finlib.valuation.xirr.dto.XirrStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class XirrStatsTest {
  @Test
  public void test() {
    List<Cashflow> cashflows = Cashflows.builder()
        .cashflow(Cashflow.of(LocalDate.parse("2010-01-01"), 1000.0d, Currency.EUR))
        .cashflow(Cashflow.of(LocalDate.parse("2010-01-02"), 200.0d, Currency.EUR))
        .cashflow(Cashflow.of(LocalDate.parse("2010-01-03"), -900.0d, Currency.EUR))
        .cashflow(Cashflow.of(LocalDate.parse("2010-01-03"), -50.0d, Currency.EUR))
        .build().getCashflows();
    var stats = XirrStats.fromCashflows(cashflows);
    Assertions.assertEquals(1200d, stats.getIncomes());
    Assertions.assertEquals(950d, stats.getOutcomes());
    Assertions.assertEquals(1000d, stats.getMaxAmount());
    Assertions.assertEquals(-900d, stats.getMinAmount());
    Assertions.assertEquals(250d, stats.getTotal());
    Assertions.assertEquals(LocalDate.parse("2010-01-01"), stats.getStartDate());
    Assertions.assertEquals(LocalDate.parse("2010-01-03"), stats.getEndDate());
  }

}
