package org.blacksmith.finlib.rates.fxrates;

import org.blacksmith.finlib.rates.marketdata.MarketDataService;

public interface FxRateProvider extends MarketDataService<FxRateId, FxRate3RSource.FxRate3RawValue> {
}
