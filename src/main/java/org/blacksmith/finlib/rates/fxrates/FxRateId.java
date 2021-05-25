package org.blacksmith.finlib.rates.fxrates;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rates.marketdata.MarketDataId;

import lombok.Value;

@Value
public class FxRateId implements MarketDataId {
  private final String table;
  private final Currency fromCcy;
  private final Currency toCcy;

  public FxRateId(String table, Currency fromCcy, Currency toCcy) {
    ArgChecker.notEmpty(table);
    ArgChecker.notNull(fromCcy);
    ArgChecker.notNull(toCcy);
    this.table = table;
    this.fromCcy = fromCcy;
    this.toCcy = toCcy;
  }

  public static FxRateId of(String fromCcy, String toCcy) {
    return new FxRateId("DEFAULT", Currency.of(fromCcy), Currency.of(toCcy));
  }

  public static FxRateId of(String name) {
    ArgChecker.notEmpty(name);
    ArgChecker.isTrue(name.length()==7,"Name should have size of 7 characters");
    String fromCcy = name.substring(0,3);
    String toCcy = name.substring(4, 7);
    return new FxRateId("DEFAULT", Currency.of(fromCcy), Currency.of(toCcy));
  }

  public static FxRateId of(String table, String fromCcy, String toCcy) {
    return new FxRateId(table, Currency.of(fromCcy), Currency.of(toCcy));
  }

  public static FxRateId of(Currency fromCcy, Currency toCcy) {
    return new FxRateId("DEFAULT", fromCcy, toCcy);
  }

  public static FxRateId of(String table, Currency fromCcy, Currency toCcy) {
    return new FxRateId(table, fromCcy, toCcy);
  }

  public String getPairName() {
    return fromCcy.getCurrencyCode() + "/" + toCcy.getCurrencyCode();
  }

  public FxRateId inverse() {
    return new FxRateId(this.table, this.toCcy, this.fromCcy);
  }
}
