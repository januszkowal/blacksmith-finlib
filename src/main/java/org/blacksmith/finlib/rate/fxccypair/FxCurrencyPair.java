package org.blacksmith.finlib.rate.fxccypair;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.currency.CurrencyPair;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FxCurrencyPair extends CurrencyPair {

  @ToString.Include
  private final boolean isCross;
  @ToString.Include
  private final double factor;

  public FxCurrencyPair(Currency base, Currency counter, boolean isCross, double factor) {
    super(base, counter);
    this.isCross = isCross;
    this.factor = factor;
  }

  public static FxCurrencyPair of(Currency base, Currency counter, boolean isCross, double factor) {
    return new FxCurrencyPair(base, counter, isCross, factor);
  }

  public static FxCurrencyPair of(String base, String counter, boolean isCross, double factor) {
    return new FxCurrencyPair(Currency.of(base), Currency.of(counter), isCross, factor);
  }

  public double getFactor() {
    return factor;
  }
}
