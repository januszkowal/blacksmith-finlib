package org.blacksmith.finlib.rate.fxccypair;

import org.blacksmith.finlib.rate.fxrate.FxRateId;

public interface FxCurrencyPairProvider {
  CurrencyPairExt getPair(String base, String counter);

  default CurrencyPairExt getPair(FxRateId pair) {
    return getPair(pair.getBase().getCurrencyCode(), pair.getCounter().getCurrencyCode());
  }
}
