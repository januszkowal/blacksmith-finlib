package org.blacksmith.finlib.rates.basic;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.blacksmith.finlib.rates.MarketData;
import org.blacksmith.finlib.rates.MarketDataId;
import org.blacksmith.finlib.rates.MarketDataService;

public class MarketDataMemoryService<K extends MarketDataId, V>
    implements MarketDataService<K, V> {

  private final Map<K, List<MarketDataHolder<K, V>>> marketData = new HashMap<>();

  public void setMarketData(List<MarketDataHolder<K, V>> m) {
    this.marketData.clear();
    marketData.putAll(m.stream().collect(Collectors.groupingBy(MarketDataHolder::getKey)));
  }

  @Override
  public MarketData<V> getRate(MarketDataId key, LocalDate date) {
    return marketData.getOrDefault(key, Collections.emptyList()).stream()
        .filter(m->m.getMarketData().getDate().compareTo(date) <= 0)
        .max(MarketDataHolder.marketDataDateComparator)
        .map(MarketDataHolder::getMarketData)
        .orElse(null);
  }
}
