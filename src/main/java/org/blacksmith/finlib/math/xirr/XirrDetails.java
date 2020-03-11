package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;

/**
 * Converts a stream of {@link Cashflow} instances into the data needed for
 * the {@link Xirr} algorithm.
 * */
class XirrDetails {
  public static XirrDetails calculateFromCashflows1(Collection<Cashflow> csws) {
    final XirrDetails result = new XirrDetails();
    result.start = csws.stream().map(Cashflow::getDate).min(LocalDate::compareTo).orElse(null);
    result.end = csws.stream().map(Cashflow::getDate).max(LocalDate::compareTo).orElse(null);
    final DoubleSummaryStatistics amountStatistics = csws.stream()
        .collect(Collectors.summarizingDouble(Cashflow::getAmount));
    result.minAmount = amountStatistics.getMin();
    result.maxAmount = amountStatistics.getMax();
    result.total = amountStatistics.getSum();
    result.incomes = csws.stream().filter(csw->csw.getAmount()>0.0)
        .collect(Collectors.summingDouble(Cashflow::getAmount));
    result.outcomes = -csws.stream().filter(csw->csw.getAmount()<0.0)
        .collect(Collectors.summingDouble(Cashflow::getAmount));
    return result;
  }

  public static XirrDetails calculateFromCashflows2(Collection<Cashflow> csws) {
    final XirrDetails result = new XirrDetails();
    for (Cashflow csw: csws) {
      result.start = result.start != null && result.start.isBefore(csw.date) ? result.start : csw.date;
      result.end   = result.end   != null && result.end.isAfter(csw.date) ? result.end : csw.date;
      result.minAmount = Math.min(result.minAmount, csw.getAmount());
      result.maxAmount = Math.max(result.maxAmount, csw.getAmount());
      result.total += csw.getAmount();
      if (csw.getAmount() < 0) {
        result.outcomes -= csw.getAmount();
      } else if (csw.getAmount() > 0) {
        result.incomes -= csw.getAmount();
      }
    }
    return result;
  }


  LocalDate start;
  LocalDate end;
  double minAmount = Double.POSITIVE_INFINITY;
  double maxAmount = Double.NEGATIVE_INFINITY;
  double total;
  double outcomes;
  double incomes;

  public void validate() {
    if (start == null) {
      throw new IllegalArgumentException("No cashflows to anaylze");
    }

    if (start.equals(end)) {
      throw new IllegalArgumentException(
          "Cashflows must not all be on the same day.");
    }
    if (minAmount >= 0) {
      throw new IllegalArgumentException(
          "Cashflows must not all be nonnegative.");
    }
    if (maxAmount < 0) {
      throw new IllegalArgumentException(
          "Cashflows must not all be positive.");
    }
  }

}
