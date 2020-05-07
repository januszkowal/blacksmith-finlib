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

  /**
   * Construct a Transaction instance with the given amount at the given day.
   * @param amount the amount transferred
   * @param date the day the transaction took place
   */
  public Cashflow(LocalDate date, double amount) {
    this.date = date;
    this.amount = amount;
  }

  public static Cashflow of (LocalDate date,double amount) {
    return new Cashflow(date,amount);
  }

  /**
   * The day the cashlfow took place.
   * @return day
   */
  public LocalDate getDate() {
    return this.date;
  }

  /**
   * The amount of cashlfow
   * @return amount
   */
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