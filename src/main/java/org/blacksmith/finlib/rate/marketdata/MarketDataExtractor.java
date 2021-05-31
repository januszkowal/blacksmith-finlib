package org.blacksmith.finlib.rate.marketdata;

public interface MarketDataExtractor<V, R> {
  R extract(MarketData<V> value);
}
