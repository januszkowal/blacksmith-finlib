package org.blacksmith.finlib.rate.fxrate.impl;

import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.ToString;
import lombok.Value;

@ToString
@Value
class FxCurrencyPairInternal {
  private final FxRateId fxRateId;
  private final boolean isCross;
  private final boolean isDirect;
  private final double factor;

  public FxCurrencyPairInternal(FxRateId fxRateId, boolean isCross, boolean isDirect, double factor) {
    this.fxRateId = fxRateId;
    this.isCross = isCross;
    this.isDirect = isDirect;
    this.factor = factor;
  }
}
