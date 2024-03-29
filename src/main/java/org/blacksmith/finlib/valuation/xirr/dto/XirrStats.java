package org.blacksmith.finlib.valuation.xirr.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.stream.Collector;

import org.blacksmith.finlib.valuation.dto.Cashflow;
import org.blacksmith.finlib.valuation.xirr.XirrCalculator;

/**
 * Converts a stream of {@link Cashflow} instances into the data needed for
 * the {@link XirrCalculator} algorithm.
 */
public class XirrStats {
  LocalDate startDate;
  LocalDate endDate;
  double minAmount = Double.POSITIVE_INFINITY;
  double maxAmount = Double.NEGATIVE_INFINITY;
  double total = 0.0;
  double incomes = 0.0;
  double outcomes = 0.0;

  public static Collector<Cashflow, XirrStats, XirrStats> collector() {
    return Collector.of(
        XirrStats::new,
        XirrStats::accumulate,
        XirrStats::combine,
        Collector.Characteristics.IDENTITY_FINISH,
        Collector.Characteristics.UNORDERED);
  }

  private static void accumulate(XirrStats a, Cashflow cs) {
    if (a.startDate == null) {
      a.startDate = cs.getDate();
      a.endDate = cs.getDate();
    }
    else {
      a.startDate = cs.getDate().isBefore(a.startDate) ? cs.getDate() : a.startDate;
      a.endDate = cs.getDate().isAfter(a.endDate) ? cs.getDate() : a.endDate;
    }
    a.minAmount = Math.min(a.minAmount, cs.getAmount().doubleValue());
    a.maxAmount = Math.max(a.maxAmount, cs.getAmount().doubleValue());
    a.total += cs.getAmount().doubleValue();
    if (cs.getAmount().doubleValue() < 0) {
      a.outcomes -= cs.getAmount().doubleValue();
    } else {
      a.incomes += cs.getAmount().doubleValue();
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
