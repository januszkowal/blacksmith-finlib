package org.blacksmith.finlib.marketdata;

public interface SingleDateMarketDataProvider<I extends MarketDataId<V>, V extends MarketData>  {
  V getValue(I id);
}
