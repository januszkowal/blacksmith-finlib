package org.blacksmith.finlib.valuation.dto;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CashflowAggregator {
  public static Collector<Cashflow, ?, BigDecimal> summingBigDecimal() {
    return Collector.of(
        () -> new BigDecimal[]{ BigDecimal.ZERO },
        (a, t) -> {
          a[0] = a[0].add(t.getAmount());
        },
        (a, b) -> new BigDecimal[]{ a[0].add(b[0]) },
        sum -> sum[0]);
  }

  public static List<Cashflow> aggregate(List<Cashflow> cashflows) {
    return aggregate(cashflows, false);
  }

  public static List<Cashflow> aggregate(List<Cashflow> cashflows, boolean removeZero) {
    return cashflows.stream()
        .collect(Collectors.groupingBy(Cashflow::getCurrency,
            Collectors.groupingBy(Cashflow::getDate, summingBigDecimal())))
        .entrySet().stream()
        .flatMap(gc -> gc.getValue().entrySet().stream()
            .filter(xx -> (!removeZero) || (removeZero && xx.getValue().compareTo(BigDecimal.ZERO) != 0))
            .map(gd -> Cashflow.of(gd.getKey(), gd.getValue(), gc.getKey())))
        .collect(Collectors.toList());
  }

  public static List<Cashflow> byCcy(List<Cashflow> cashflows, Currency currency) {
    return cashflows.stream().filter(cashflow -> cashflow.getCurrency().equals(currency)).collect(Collectors.toList());
  }
}
