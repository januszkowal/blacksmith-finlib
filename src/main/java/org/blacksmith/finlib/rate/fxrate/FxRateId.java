package org.blacksmith.finlib.rate.fxrate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.marketdata.MarketDataId;

import lombok.Value;

@Value
public final class FxRateId implements MarketDataId<FxRate3> {
  String table;
  Currency baseCcy;
  Currency counterCcy;

  public FxRateId(String table, Currency baseCcy, Currency counterCcy) {
    ArgChecker.notEmpty(table);
    ArgChecker.notNull(baseCcy);
    ArgChecker.notNull(counterCcy);
    this.table = table;
    this.baseCcy = baseCcy;
    this.counterCcy = counterCcy;
  }

  public static FxRateId of(String baseCcy, String counterCcy) {
    return new FxRateId("DEFAULT", Currency.of(baseCcy), Currency.of(counterCcy));
  }

  public static FxRateId of(String name) {
    ArgChecker.notEmpty(name);
    ArgChecker.isTrue(name.length()==7,"Name should have size of 7 characters");
    String baseCcy = name.substring(0,3);
    String counterCcy = name.substring(4, 7);
    return new FxRateId("DEFAULT", Currency.of(baseCcy), Currency.of(counterCcy));
  }

  public static FxRateId of(String table, String baseCcy, String toCcy) {
    return new FxRateId(table, Currency.of(baseCcy), Currency.of(toCcy));
  }

  public static FxRateId of(Currency baseCcy, Currency counterCcy) {
    return new FxRateId("DEFAULT", baseCcy, counterCcy);
  }

  public static FxRateId of(String table, Currency baseCcy, Currency counterCcy) {
    return new FxRateId(table, baseCcy, counterCcy);
  }

  public String getPairName() {
    return baseCcy.getCurrencyCode() + "/" + counterCcy.getCurrencyCode();
  }

  public FxRateId inverse() {
    return new FxRateId(this.table, this.counterCcy, this.baseCcy);
  }

  @Override
  public Class<FxRate3> getMarketDataType() {
    return FxRate3.class;
  }
}
