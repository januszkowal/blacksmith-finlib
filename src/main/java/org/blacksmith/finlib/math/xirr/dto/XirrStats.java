package org.blacksmith.finlib.math.xirr.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.DoubleSummaryStatistics;
import java.util.stream.Collectors;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.Xirr;

/**
 * Converts a stream of {@link Cashflow} instances into the data needed for
 * the {@link Xirr} algorithm.
 * */
public class XirrStats {
  public static XirrStats fromCashflows(Collection<Cashflow> csws) {
    final XirrStats result = new XirrStats();
    result.startDate = csws.stream().map(Cashflow::getDate).min(LocalDate::compareTo).orElse(null);
    result.endDate = csws.stream().map(Cashflow::getDate).max(LocalDate::compareTo).orElse(null);
    final DoubleSummaryStatistics amountStatistics = csws.stream()
        .collect(Collectors.summarizingDouble(Cashflow::getAmount));
    result.minAmount = amountStatistics.getMin();
    result.maxAmount = amountStatistics.getMax();
    result.total = amountStatistics.getSum();
    result.incomes = csws.stream().filter(csw->csw.getAmount()>0.0)
        .collect(Collectors.summingDouble(Cashflow::getAmount));
    result.outcomes = -csws.stream().filter(csw->csw.getAmount()<0.0)
        .collect(Collectors.summingDouble(Cashflow::getAmount));
    result.validate();
    return result;
  }

  LocalDate startDate;
  LocalDate endDate;
  double minAmount = Double.POSITIVE_INFINITY;
  double maxAmount = Double.NEGATIVE_INFINITY;
  double total;
  double outcomes;

  public LocalDate getStartDate() {
    return startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public double getMinAmount() {
    return minAmount;
  }

  public double getMaxAmount() {
    return maxAmount;
  }

  public double getTotal() {
    return total;
  }

  public double getOutcomes() {
    return outcomes;
  }

  public double getIncomes() {
    return incomes;
  }

  double incomes;

  public void validate() {
    if (startDate == null) {
      throw new IllegalArgumentException("No cashflows to anaylze");
    }

    if (startDate.equals(endDate)) {
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
