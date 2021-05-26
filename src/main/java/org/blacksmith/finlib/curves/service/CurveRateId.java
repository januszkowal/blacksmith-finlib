package org.blacksmith.finlib.curves.service;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rates.marketdata.MarketDataId;

import lombok.Value;

@Value
public class CurveRateId implements MarketDataId {
  private final String curve;
  private final Currency currency;

  public CurveRateId(String curve, Currency currency) {
    ArgChecker.notEmpty(curve);
    ArgChecker.notNull(currency);
    this.curve = curve;
    this.currency = currency;
  }

  public static CurveRateId of(String curve, Currency currency) {
    return new CurveRateId(curve, currency);
  }

  public static CurveRateId of(String curve, String currency) {

    return new CurveRateId(curve, Currency.of(currency));
  }
}
