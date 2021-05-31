package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MarketDataMemoryService<K, V> implements MarketDataService<K, V> {

  private final Map<K, List<MarketDataWrapper<K, V>>> marketData = new HashMap<>();

  public MarketDataMemoryService() {
  }

  public MarketDataMemoryService(List<MarketDataWrapper<K, V>> marketData) {
    setMarketData(marketData);
  }

  public void setMarketData(List<MarketDataWrapper<K, V>> m) {
    this.marketData.clear();
    this.marketData.putAll(m.stream().collect(Collectors.groupingBy(MarketDataWrapper::getKey)));
  }

  @Override
  public MarketData<V> getRate(K key, LocalDate date) {
    return marketData.getOrDefault(key, Collections.emptyList()).stream()
        .filter(m -> m.getMarketData().getDate().compareTo(date) <= 0)
        .max(MarketDataWrapper.marketDataDateComparator)
        .map(MarketDataWrapper::getMarketData)
        .orElse(null);
  }

  public List<MarketDataWrapper<K, V>> getMarketData() {
    return this.marketData.values().stream().flatMap(x -> x.stream()).collect(Collectors.toList());
  }
}
