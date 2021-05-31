package org.blacksmith.finlib.rate.fxrate;

import org.blacksmith.finlib.rate.marketdata.MarketDataProvider;

public interface FxRateProvider extends MarketDataProvider<FxRateId, FxRate3RSource.FxRate3RawValue> {
}
