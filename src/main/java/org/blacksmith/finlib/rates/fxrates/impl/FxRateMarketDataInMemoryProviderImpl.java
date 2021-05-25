package org.blacksmith.finlib.rates.fxrates.impl;

import java.util.List;

import org.blacksmith.finlib.rates.fxrates.FxRate3RSource;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateProvider;
import org.blacksmith.finlib.rates.marketdata.MarketDataInMemoryProvider;
import org.blacksmith.finlib.rates.marketdata.MarketDataWrapper;

public class FxRateMarketDataInMemoryProviderImpl extends MarketDataInMemoryProvider<FxRateId, FxRate3RSource.FxRate3RawValue>
    implements FxRateProvider {

  public FxRateMarketDataInMemoryProviderImpl() {}

  public FxRateMarketDataInMemoryProviderImpl(List<MarketDataWrapper<FxRateId, FxRate3RSource.FxRate3RawValue>> marketData) {
    setMarketData(marketData);
  }
}
