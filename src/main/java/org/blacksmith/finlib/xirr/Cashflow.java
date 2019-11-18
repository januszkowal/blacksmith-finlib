package org.blacksmith.finlib.xirr;

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
public class Cashflow {

  final double amount;
  final LocalDate date;

  /**
   * Construct a Transaction instance with the given amount at the given day.
   * @param amount the amount transferred
   * @param date the day the transaction took place
   */
  public Cashflow(double amount, LocalDate date) {
    this.amount = amount;
    this.date = date;
  }

  /**
   * The amount of cashlfow
   * @return amount
   */
  public double getAmount() {
    return amount;
  }

  /**
   * The day the cashlfow took place.
   * @return day
   */
  public LocalDate getDate() {
    return this.date;
  }
}
