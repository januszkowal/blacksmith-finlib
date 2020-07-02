package org.blacksmith.finlib.rates.fxrates;

import lombok.Value;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.rates.MarketDataId;

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

  public static FxRateId of (String fromCcy, String toCcy) {
    return new FxRateId("DEFAULT", Currency.of(fromCcy),Currency.of(toCcy));
  }
  public static FxRateId of (String table, String fromCcy, String toCcy) {
    return new FxRateId(table, Currency.of(fromCcy),Currency.of(toCcy));
  }

  public static FxRateId of (Currency fromCcy, Currency toCcy) {
    return new FxRateId("DEFAULT", fromCcy,toCcy);
  }
  public static FxRateId of (String table, Currency fromCcy, Currency toCcy) {
    return new FxRateId(table, fromCcy,toCcy);
  }

  public String getPairName() {
    return fromCcy + "/" + toCcy;
  }

  public FxRateId inverse() {
    return new FxRateId(this.table, this.toCcy,this.fromCcy);
  }
}
