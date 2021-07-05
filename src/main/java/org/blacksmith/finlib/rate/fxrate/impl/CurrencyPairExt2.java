package org.blacksmith.finlib.rate.fxrate.impl;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.Value;

@Value(staticConstructor ="of")
public class CurrencyPairExt2 {
  Currency base;
  Currency counter;
  boolean isCross;
  double factor;
  boolean isDirect;

  public FxRateId getFxRateId() {
    return FxRateId.of(this.getBase(), this.getCounter());
  }
}
