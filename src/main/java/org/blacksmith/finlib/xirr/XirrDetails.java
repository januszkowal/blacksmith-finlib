package org.blacksmith.finlib.xirr;

import java.time.LocalDate;
import java.util.stream.Collector;

/**
 * Converts a stream of {@link Cashflow} instances into the data needed for
 * the {@link Xirr} algorithm.
 * */
class XirrDetails {
  public static Collector<Cashflow, XirrDetails, XirrDetails> collector() {
    return Collector.of(
        XirrDetails::new,
        XirrDetails::accumulate,
        XirrDetails::combine,
        Collector.Characteristics.IDENTITY_FINISH,
        Collector.Characteristics.UNORDERED);
  }

  LocalDate start;
  LocalDate end;
  double minAmount = Double.POSITIVE_INFINITY;
  double maxAmount = Double.NEGATIVE_INFINITY;
  double total;
  double outcomes;
  double incomes;

  public void accumulate(final Cashflow tx) {
    start = start != null && start.isBefore(tx.date) ? start : tx.date;
    end = end != null && end.isAfter(tx.date) ? end : tx.date;
    minAmount = Math.min(minAmount, tx.amount);
    maxAmount = Math.max(maxAmount, tx.amount);
    total += tx.amount;
    if (tx.amount < 0) {
      outcomes -= tx.amount;
    }
    else if (tx.amount > 0) {
      incomes -= tx.amount;
    }
  }

  public XirrDetails combine(final XirrDetails other) {
    start = start.isBefore(other.start) ? start : other.start;
    end = end.isAfter(other.end) ? end : other.end;
    minAmount = Math.min(minAmount, other.minAmount);
    maxAmount = Math.max(maxAmount, other.maxAmount);
    total += other.total;
    total += other.total;
    return this;
  }

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
