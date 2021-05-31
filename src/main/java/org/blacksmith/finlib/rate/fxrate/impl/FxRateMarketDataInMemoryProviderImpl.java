package org.blacksmith.finlib.rate.fxrate.impl;

import java.util.List;

import org.blacksmith.finlib.rate.fxrate.FxRate3RSource;
import org.blacksmith.finlib.rate.fxrate.FxRateId;
import org.blacksmith.finlib.rate.fxrate.FxRateProvider;
import org.blacksmith.finlib.rate.marketdata.MarketDataInMemoryProvider;
import org.blacksmith.finlib.rate.marketdata.MarketDataWrapper;

public class FxRateMarketDataInMemoryProviderImpl extends MarketDataInMemoryProvider<FxRateId, FxRate3RSource.FxRate3RawValue>
    implements FxRateProvider {

  public FxRateMarketDataInMemoryProviderImpl() {}

  public FxRateMarketDataInMemoryProviderImpl(List<MarketDataWrapper<FxRateId, FxRate3RSource.FxRate3RawValue>> marketData) {
    setMarketData(marketData);
  }
}
