package org.blacksmith.finlib.rates.interestrates;

import lombok.Value;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rates.MarketDataId;

@Value(staticConstructor = "of")
public class InterestRateId implements MarketDataId {
  //table
  public final String table;
  //period
  public final String period;
  //currency
  public final Currency currency;

  public InterestRateId(String table, String period, Currency currency) {
    ArgChecker.notNull(table);
    ArgChecker.notNull(period);
    ArgChecker.notNull(currency);
    this.table = table;
    this.period = period;
    this.currency = currency;
  }
}
