package org.blacksmith.finlib.rates.curves.service;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rates.MarketDataId;

import lombok.Value;

@Value
public class CurveRateId implements MarketDataId {
  private final String curve;
  private final Currency ccy;

  public CurveRateId(String curve, Currency ccy) {
    ArgChecker.notEmpty(curve);
    ArgChecker.notNull(ccy);
    this.curve = curve;
    this.ccy = ccy;
  }

  public static CurveRateId of (String curve, Currency ccy) {
    return new CurveRateId(curve, ccy);
  }
  public static CurveRateId of (String curve, String ccy) {
    return new CurveRateId(curve, Currency.of(ccy));
  }
}
