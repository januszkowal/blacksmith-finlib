package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;

import lombok.ToString;

@ToString
public class BasicMarketDataWrapper<K, V> implements MarketDataWrapper<K, V> {

  private final K key;
  private final MarketData<V> marketData;

  public BasicMarketDataWrapper(K key, MarketData<V> marketData) {
    this.key = key;
    this.marketData = marketData;
  }

  public static <K, V> BasicMarketDataWrapper<K, V> of(K key, MarketData<V> marketData) {
    return new BasicMarketDataWrapper<>(key, marketData);
  }

  public static <K, V> BasicMarketDataWrapper<K, V> of(K key, LocalDate date, V value) {
    return new BasicMarketDataWrapper<>(key, new BasicMarketData<>(date, value));
  }

  @Override
  public K getKey() {
    return this.key;
  }

  @Override
  public MarketData<V> getMarketData() {
    return this.marketData;
  }
}
