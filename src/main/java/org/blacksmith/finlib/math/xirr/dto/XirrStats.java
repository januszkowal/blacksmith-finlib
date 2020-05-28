package org.blacksmith.finlib.math.xirr.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collector;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.XirrCalculator;

/**
 * Converts a stream of {@link Cashflow} instances into the data needed for
 * the {@link XirrCalculator} algorithm.
 * */
public class XirrStats {
  public static Collector<Cashflow, XirrStats, XirrStats> collector() {
    return Collector.of(
        XirrStats::new,
        XirrStats::accumulate,
        XirrStats::combine,
        Collector.Characteristics.IDENTITY_FINISH,
        Collector.Characteristics.UNORDERED);
  }

  private static void accumulate(XirrStats a, Cashflow cs) {
    a.startDate = a.startDate != null && a.startDate.isBefore(cs.getDate()) ? a.startDate : cs.getDate();
    a.endDate   = a.endDate != null && a.endDate.isAfter(cs.getDate()) ? a.endDate : cs.getDate();
    a.minAmount = Math.min(a.minAmount, cs.getAmount());
    a.maxAmount = Math.max(a.maxAmount, cs.getAmount());
    a.total += cs.getAmount();
    if (cs.getAmount() < 0) {
      a.outcomes -= cs.getAmount();
    }
    else {
      a.incomes += cs.getAmount();
    }
  }

  private static XirrStats combine(XirrStats a, XirrStats b) {
    a.startDate = a.startDate.isBefore(b.startDate) ? a.startDate : b.startDate;
    a.endDate = a.endDate.isAfter(b.endDate) ? a.endDate : b.endDate;
    a.minAmount = Math.min(a.minAmount, b.minAmount);
    a.maxAmount = Math.max(a.maxAmount, b.maxAmount);
    a.total += b.total;
    a.outcomes += b.outcomes;
    a.incomes += b.incomes;
    return a;
  }

  public static XirrStats fromCashflows(Collection<Cashflow> cashflows) {
    XirrStats result = cashflows.stream().collect(XirrStats.collector());
    result.validate();
    return result;
  }

  LocalDate startDate;
  LocalDate endDate;
  double minAmount = Double.POSITIVE_INFINITY;
  double maxAmount = Double.NEGATIVE_INFINITY;
  double total = 0.0;
  double incomes = 0.0;
  double outcomes = 0.0;

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

  public void validate() {
    if (startDate == null) {
      throw new IllegalArgumentException("No cashflows to analyze");
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
