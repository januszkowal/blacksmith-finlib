package org.blacksmith.finlib.rate.fxrate;

import java.util.List;

import org.blacksmith.finlib.marketdata.MarketDataInMemoryProvider;
import org.blacksmith.finlib.rate.marketdata.MarketDataWrapper;

public class FxRateMarketDataInMemoryProviderImpl extends MarketDataInMemoryProvider<FxRateId, FxRate3>
    implements FxRateProvider {

  public FxRateMarketDataInMemoryProviderImpl() {}

  public FxRateMarketDataInMemoryProviderImpl(List<MarketDataWrapper<FxRateId, FxRate3>> marketData) {
    setMarketData(marketData);
  }
}
