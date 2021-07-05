package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MarketDataInMemoryProvider<I extends MarketDataId<V>, V extends MarketData> implements MarketDataProvider<I, V> {

  private final Map<I, List<MarketDataWrapper<I, V>>> marketData = new ConcurrentHashMap<>();

  public MarketDataInMemoryProvider() {
  }

  public MarketDataInMemoryProvider(List<MarketDataWrapper<I, V>> marketData) {
    setMarketData(marketData);
  }

  public void setMarketData(List<MarketDataWrapper<I, V>> m) {
    this.marketData.clear();
    this.marketData.putAll(m.stream().collect(Collectors.groupingBy(MarketDataWrapper::getId)));
  }

  @Override
  public V get(I id, LocalDate date) {
    return marketData.getOrDefault(id, Collections.emptyList()).stream()
        .filter(m -> m.getValue().getDate().compareTo(date) <= 0)
        .max(MarketDataWrapper.marketDataDateComparator)
        .map(MarketDataWrapper::getValue)
        .orElse(null);
  }
}
