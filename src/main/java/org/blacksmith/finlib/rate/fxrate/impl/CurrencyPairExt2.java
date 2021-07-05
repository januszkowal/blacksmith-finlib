package org.blacksmith.finlib.rate.fxrate.impl;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CurrencyPairExt2 extends CurrencyPairExt {
  @ToString.Include
  private boolean isDirect;

  public CurrencyPairExt2(Currency base, Currency counter, boolean isCross, double factor, boolean isDirect) {
    super(base, counter, isCross, factor);
    this.isDirect = isDirect;
  }

  public static CurrencyPairExt2 of(Currency base, Currency counter, boolean isCross, double factor, boolean isDirect) {
    return new CurrencyPairExt2(base, counter, isCross, factor, isDirect);
  }
}
