package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;

/**
 * Represents a single cashlow for the purposes of computing the irregular rate
 * of return.
 * <p>
 * Note that negative amounts represent deposits into the investment (and so
 * withdrawals from your cash).  Positive amounts represent withdrawals from the
 * investment (deposits into cash).  Zero amounts are allowed in case your
 * investment is now worthless.
 * @see Xirr
 */
public final class Cashflow {

  final LocalDate date;
  final double amount;

  public Cashflow(LocalDate date, double amount) {
    this.date = date;
    this.amount = amount;
  }

  public static Cashflow of (LocalDate date,double amount) {
    return new Cashflow(date,amount);
  }

  public LocalDate getDate() {
    return this.date;
  }

  public double getAmount() {
    return amount;
  }

  @Override public String toString() {
    return "Cashflow{" +
        "date=" + date +
        ", amount=" + amount +
        '}';
  }
}
