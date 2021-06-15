package org.blacksmith.finlib.valuation.dto;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.valuation.xirr.XirrCalculator;

/**
 * Represents a single cashlow for the purposes of computing the irregular rate
 * of return.
 * <p>
 * Note that negative amounts represent deposits into the investment (and so
 * withdrawals from your cash).  Positive amounts represent withdrawals from the
 * investment (deposits into cash).  Zero amounts are allowed in case your
 * investment is now worthless.
 *
 * @see XirrCalculator
 */
public final class Cashflow {

  private final LocalDate date;
  private final double amount;

  public Cashflow(LocalDate date, double amount) {
    this.date = date;
    this.amount = amount;
  }

  public static Cashflow of(LocalDate date, double amount) {
    return new Cashflow(date, amount);
  }

  public LocalDate getDate() {
    return this.date;
  }

  public double getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    return "Cashflow{" +
        "date=" + date +
        ", amount=" + amount +
        '}';
  }

  public static List<Cashflow> groupCashflows(Collection<Cashflow> cashflows) {
    return cashflows.stream()
        .collect(Collectors.groupingBy(Cashflow::getDate, Collectors.summingDouble(Cashflow::getAmount)))
        .entrySet().stream()
        .map(e -> Cashflow.of(e.getKey(), e.getValue()))
        .sorted(Comparator.comparing(Cashflow::getDate))
        .collect(Collectors.toList());
  }
}
