package org.blacksmith.finlib.rates.fxccypair;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.blacksmith.finlib.basic.Currency;
import org.blacksmith.finlib.basic.CurrencyPair;

@Getter
@EqualsAndHashCode(callSuper = true)
public class FxCurrencyPair extends CurrencyPair {

  private final boolean isCross;
  private final double factor;
  private final boolean isFactor;

  public FxCurrencyPair(Currency base, Currency counter, boolean isCross, double factor) {
    super(base,counter);
    this.isCross = isCross;
    this.factor = factor;
    this.isFactor = factor != 1.0d;
  }

  public static FxCurrencyPair of(Currency base, Currency counter, boolean isCross, double factor) {
    return new FxCurrencyPair(base, counter,isCross,factor);
  }

  public double getFactor() {
    return factor;
  }

  public boolean isFactor() {
    return this.isFactor;
  }
}
