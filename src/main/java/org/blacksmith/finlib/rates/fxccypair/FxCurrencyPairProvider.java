package org.blacksmith.finlib.rates.fxccypair;

import org.blacksmith.finlib.basic.currency.Currency;

public interface FxCurrencyPairProvider {
  FxCurrencyPair getPair(String ccy1, String ccy2);
  default FxCurrencyPair getPair(Currency ccy1, Currency ccy2) {
    return getPair(ccy1.getCurrencyCode(), ccy2.getCurrencyCode());
  }
}
