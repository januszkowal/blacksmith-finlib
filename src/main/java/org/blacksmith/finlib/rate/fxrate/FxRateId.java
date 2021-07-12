package org.blacksmith.finlib.rate.fxrate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.marketdata.MarketDataId;

import lombok.Value;

@Value
public final class FxRateId implements MarketDataId<FxRate3> {
  private static final String DEFAULT = "DEFAULT";
  Currency base;
  Currency counter;
  String source;

  public FxRateId(Currency base, Currency counter, String source) {
    ArgChecker.notNull(base);
    ArgChecker.notNull(counter);
    ArgChecker.notEmpty(source);
    this.base = base;
    this.counter = counter;
    this.source = source;
  }

  public static FxRateId of(String base, String counter) {
    return new FxRateId(Currency.of(base), Currency.of(counter), DEFAULT);
  }

  public static FxRateId of(String name) {
    ArgChecker.notEmpty(name);
    ArgChecker.isTrue(name.length()==7,"Name should have size of 7 characters");
    String baseCcy = name.substring(0,3);
    String counterCcy = name.substring(4, 7);
    return new FxRateId(Currency.of(baseCcy), Currency.of(counterCcy), DEFAULT);
  }

  public static FxRateId of(String base, String counterCcy, String table) {
    return new FxRateId(Currency.of(base), Currency.of(counterCcy), DEFAULT);
  }

  public static FxRateId of(Currency base, Currency counter) {
    return new FxRateId(base, counter, DEFAULT);
  }

  public static FxRateId of(Currency base, Currency counter, String source) {
    return new FxRateId(base, counter, DEFAULT);
  }

  public String getPairName() {
    return base.getCurrencyCode() + "/" + counter.getCurrencyCode();
  }

  public FxRateId inverse() {
    return new FxRateId(this.counter, this.base, this.source);
  }

  @Override
  public Class<FxRate3> getMarketDataType() {
    return FxRate3.class;
  }
}
