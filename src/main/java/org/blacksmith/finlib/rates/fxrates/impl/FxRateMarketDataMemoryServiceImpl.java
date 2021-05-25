package org.blacksmith.finlib.rates.fxrates.impl;

import java.util.List;

import org.blacksmith.finlib.rates.fxrates.FxRate3RSource;
import org.blacksmith.finlib.rates.fxrates.FxRateId;
import org.blacksmith.finlib.rates.fxrates.FxRateProvider;
import org.blacksmith.finlib.rates.marketdata.MarketDataMemoryService;
import org.blacksmith.finlib.rates.marketdata.MarketDataWrapper;

public class FxRateMarketDataMemoryServiceImpl extends MarketDataMemoryService<FxRateId, FxRate3RSource.FxRate3RawValue>
    implements FxRateProvider {

  public FxRateMarketDataMemoryServiceImpl() {}

  public FxRateMarketDataMemoryServiceImpl(List<MarketDataWrapper<FxRateId, FxRate3RSource.FxRate3RawValue>> marketData) {
    setMarketData(marketData);
  }
}
