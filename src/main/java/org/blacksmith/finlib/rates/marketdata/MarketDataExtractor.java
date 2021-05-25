package org.blacksmith.finlib.rates.marketdata;

import org.blacksmith.finlib.rates.marketdata.MarketData;

public interface MarketDataExtractor<V, R> {
  R extract(MarketData<V> value);
}
