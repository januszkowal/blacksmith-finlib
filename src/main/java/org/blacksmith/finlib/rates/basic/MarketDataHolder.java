package org.blacksmith.finlib.rates.basic;

import java.util.Comparator;

import org.blacksmith.finlib.rates.MarketData;

public interface MarketDataHolder<K, V> {
  Comparator<MarketDataHolder<?, ?>> marketDataDateComparator =
      //      Comparator.comparing(m->m.getDate());
      Comparator.comparing(m -> m.getMarketData().getDate());

  K getKey();

  MarketData<V> getMarketData();
}
