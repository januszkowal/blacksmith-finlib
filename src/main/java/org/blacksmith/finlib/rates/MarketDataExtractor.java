package org.blacksmith.finlib.rates;

public interface MarketDataExtractor<V, R> {
  R extract(MarketData<V> value);
}
