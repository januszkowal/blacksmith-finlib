package org.blacksmith.finlib.rates.interestrates;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rates.marketdata.MarketDataId;

import lombok.Value;

@Value(staticConstructor = "of")
public class InterestRateId implements MarketDataId {
  //table
  public final String table;
  //period
  public final String period;
  //currency
  public final Currency currency;

  public InterestRateId(String table, String period, Currency currency) {
    ArgChecker.notEmpty(table, "Interest table cant be empty");
    ArgChecker.notNull(period, "Period can' be null");
    ArgChecker.notNull(currency, "Currency can't be null");
    this.table = table;
    this.period = period;
    this.currency = currency;
  }
}
