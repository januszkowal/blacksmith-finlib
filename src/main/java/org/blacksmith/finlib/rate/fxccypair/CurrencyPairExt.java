package org.blacksmith.finlib.rate.fxccypair;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.basic.currency.CurrencyPair;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CurrencyPairExt extends CurrencyPair {

  @ToString.Include
  private final boolean isCross;
  @ToString.Include
  private final double factor;

  public CurrencyPairExt(Currency base, Currency counter, boolean isCross, double factor) {
    super(base, counter);
    this.isCross = isCross;
    this.factor = factor;
  }

  public static CurrencyPairExt of(Currency base, Currency counter, boolean isCross, double factor) {
    return new CurrencyPairExt(base, counter, isCross, factor);
  }

  public static CurrencyPairExt of(String base, String counter, boolean isCross, double factor) {
    return new CurrencyPairExt(Currency.of(base), Currency.of(counter), isCross, factor);
  }

  public double getFactor() {
    return factor;
  }

  public FxRateId getFxRateId() {
    return FxRateId.of(this.getBase(), this.getCounter());
  }
}
