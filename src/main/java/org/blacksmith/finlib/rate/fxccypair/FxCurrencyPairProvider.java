package org.blacksmith.finlib.rate.fxccypair;

import org.blacksmith.finlib.rate.fxrate.FxRateId;

public interface FxCurrencyPairProvider {
  CurrencyPairExt getPair(String base, String counter);

  default CurrencyPairExt getPair(FxRateId pair) {
    return getPair(pair.getBase().getCurrencyCode(), pair.getCounter().getCurrencyCode());
  }

  default CurrencyPairExt getPair2Dir(FxRateId pair) {
    CurrencyPairExt found;
    FxRateId actualPair = pair;
    if ((found = getPair(pair)) == null) {
      actualPair = pair.inverse();
      if ((found = getPair(actualPair)) == null) {
        throw new IllegalArgumentException("Unknown pair " + pair.getPairName());
      }
      return CurrencyPairExt.of(found.getBase(), actualPair.getCounter(), found.isCross(), found.getFactor(), false);
    }
    else {
      return CurrencyPairExt.of(found.getBase(), actualPair.getCounter(), found.isCross(), found.getFactor(), true);
    }
  }
}
