package org.blacksmith.finlib.rates.marketdata;

public interface MarketDataExtractor<V, R> {
  R extract(MarketData<V> value);
}
