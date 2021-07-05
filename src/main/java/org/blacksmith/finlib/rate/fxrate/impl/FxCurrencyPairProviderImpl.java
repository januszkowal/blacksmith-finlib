package org.blacksmith.finlib.rate.fxrate.impl;

import org.blacksmith.finlib.rate.fxccypair.CurrencyPairExt;
import org.blacksmith.finlib.rate.fxccypair.FxCurrencyPairProvider;
import org.blacksmith.finlib.rate.fxrate.FxRateId;

public class FxCurrencyPairProviderImpl implements FxCurrencyPairProvider {

  private final FxCurrencyPairProvider fxCurrencyPairProvider;

  public FxCurrencyPairProviderImpl(FxCurrencyPairProvider fxCurrencyPairProvider) {
    this.fxCurrencyPairProvider = fxCurrencyPairProvider;
  }

  @Override
  public CurrencyPairExt getPair(String base, String counter) {
    return fxCurrencyPairProvider.getPair(base, counter);
  }

  @Override
  public CurrencyPairExt getPair(FxRateId pair) {
    return fxCurrencyPairProvider.getPair(pair);
  }

  public CurrencyPairExt2 getPairExt(FxRateId pair) {
    CurrencyPairExt found;
    FxRateId actualPair = pair;
    if ((found = getPair(pair)) == null) {
      actualPair = pair.inverse();
      if ((found = getPair(actualPair)) == null) {
        throw new IllegalArgumentException("Unknown pair " + pair.getPairName());
      }
      return CurrencyPairExt2.of(found.getBase(), actualPair.getCounter(), found.isCross(), found.getFactor(), false);
    }
    else {
      return CurrencyPairExt2.of(found.getBase(), actualPair.getCounter(), found.isCross(), found.getFactor(), true);
    }
  }
}
