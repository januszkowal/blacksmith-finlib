package org.blacksmith.finlib.rates.fxccypair;


public interface FxCurrencyPairProvider {
  FxCurrencyPair getPair(String ccy1, String ccy2);
}
