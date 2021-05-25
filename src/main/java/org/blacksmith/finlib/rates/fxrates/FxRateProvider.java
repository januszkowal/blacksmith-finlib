package org.blacksmith.finlib.rates.fxrates;

import org.blacksmith.finlib.rates.marketdata.MarketDataProvider;

public interface FxRateProvider extends MarketDataProvider<FxRateId, FxRate3RSource.FxRate3RawValue> {

public interface FxRateProvider extends MarketDataService<FxRateId, FxRate3RSource.FxRate3RawValue> {
}
