package org.blacksmith.finlib.rates;

import java.util.Comparator;

public interface MarketDataWrapper<K, D> {
  Comparator<MarketDataWrapper<?, ?>> marketDataDateComparator =
      Comparator.comparing(m -> m.getMarketData().getDate());

  K getKey();

  MarketData<D> getMarketData();
}
