package org.blacksmith.finlib.curve.dbstruct;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rate.marketdata.MarketDataId;

import lombok.Value;

@Value
public class CurveRateId implements MarketDataId {
  String curve;
  Currency currency;
  LocalDate asOfDate;

  public CurveRateId(String curve, Currency currency, LocalDate asOfDate) {
    ArgChecker.notEmpty(curve);
    ArgChecker.notNull(currency);
    ArgChecker.notNull(asOfDate);
    this.curve = curve;
    this.currency = currency;
    this.asOfDate = asOfDate;
  }

  public static CurveRateId of(String curve, Currency currency, LocalDate asOfDate) {
    return new CurveRateId(curve, currency, asOfDate);
  }

  public static CurveRateId of(String curve, String currency, LocalDate asOfDate) {

    return new CurveRateId(curve, Currency.of(currency), asOfDate);
  }
}
