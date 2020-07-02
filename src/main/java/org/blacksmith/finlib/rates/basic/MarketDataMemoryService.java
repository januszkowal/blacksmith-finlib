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

  private Map<K, List<BasicMarketDataHolder<K, V>>> marketData = new HashMap<K, List<BasicMarketDataHolder<K, V>>>();

  public void setMarketData(List<BasicMarketDataHolder<K, V>> m) {
    this.marketData.clear();
    marketData.putAll(m.stream().collect(Collectors.groupingBy(e -> e.getKey())));
  }

  @Override
  public MarketData<K, V> getRate(MarketDataId key, LocalDate date) {
    return marketData.getOrDefault(key, Collections.emptyList()).stream()
        .filter(m -> m.getKey().equals(key) && m.getMarketData().getDate().compareTo(date) <= 0)
        .sorted(BasicMarketDataHolder.marketDataDateComparator.reversed())
        .findFirst()
        .map(BasicMarketDataHolder::getMarketData)
        .orElse(null);
  }
}
