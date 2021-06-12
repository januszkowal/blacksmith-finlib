package org.blacksmith.finlib.marketdata;

public interface MarketDataExtractor<V extends MarketData, R> {
  R extract(V marketData);
}
