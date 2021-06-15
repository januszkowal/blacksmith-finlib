package org.blacksmith.finlib.valuation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.numbers.Amount;
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
  private final BigDecimal amount;
  private final Currency currency;

  public Cashflow(LocalDate date, BigDecimal amount, Currency currency) {
    this.date = date;
    this.amount = amount;
    this.currency = currency;
  }

  public static Cashflow of(LocalDate date, BigDecimal amount, Currency currency) {
    return new Cashflow(date, amount, currency);
  }

  public static Cashflow of(LocalDate date, double amount, Currency currency) {
    return new Cashflow(date, BigDecimal.valueOf(amount), currency);
  }

  public static List<Currency> cashflowCurrencies(Collection<Cashflow> cashflows) {
    return cashflows.stream().map(cashflow -> cashflow.currency).distinct().collect(Collectors.toList());
  }

  public LocalDate getDate() {
    return this.date;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return this.currency;
  }

  @Override
  public String toString() {
    return "Cashflow{" +
        "date=" + date +
        ", amount=" + amount + " " + currency.toString() +
        '}';
  }
}
