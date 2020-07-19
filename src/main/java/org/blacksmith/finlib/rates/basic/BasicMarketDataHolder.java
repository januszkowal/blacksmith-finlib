package org.blacksmith.finlib.rates.basic;

import java.time.LocalDate;
import lombok.ToString;
import org.blacksmith.finlib.rates.MarketData;

@ToString
public class BasicMarketDataHolder<K, V> implements MarketDataHolder<K, V> {

  private final K key;
  private final MarketData<V> marketData;

  public BasicMarketDataHolder(K key, MarketData<V> marketData) {
    this.key = key;
    this.marketData = marketData;
  }

  public static <K, V> BasicMarketDataHolder<K, V> of(K key, MarketData<V> marketData) {
    return new BasicMarketDataHolder<>(key, marketData);
  }

    public static <K, V > BasicMarketDataHolder < K, V > of(K key, LocalDate date, V value) {
      return new BasicMarketDataHolder<>(key, new BasicMarketData<>(date,value));
    }

  @Override
  public K getKey() {
    return this.key;
  }

//    @Override
//    public LocalDate getDate () {
//      return this.date;
//    }

//    @Override
//    public V getValue () {
//      return this.value;
//    }

  @Override
  public MarketData<V> getMarketData() {
    return this.marketData;
  }
}