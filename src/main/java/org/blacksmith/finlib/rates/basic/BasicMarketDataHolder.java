package org.blacksmith.finlib.rates.basic;

import java.util.Comparator;
import lombok.ToString;
import org.blacksmith.finlib.rates.MarketData;

@ToString
public class BasicMarketDataHolder<K,V> {

  public final static Comparator<BasicMarketDataHolder<?,?>> marketDataDateComparator =
      Comparator.comparing(m->m.getMarketData().getDate());

  private final K key;
  private final MarketData<K,V> marketData;

  public BasicMarketDataHolder(K key, MarketData<K,V> marketData) {
    this.key = key;
    this.marketData = marketData;
  }

  public static <K,V> BasicMarketDataHolder<K,V> of (K key, MarketData<K,V> marketData) {
    return new BasicMarketDataHolder(key,marketData);
  }
  
  public K getKey() {
    return this.key;
  }

  public MarketData<K, V> getMarketData() {
    return this.marketData;
  }
}
