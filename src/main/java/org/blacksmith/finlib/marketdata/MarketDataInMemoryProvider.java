package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.blacksmith.finlib.marketdata.MarketData;
import org.blacksmith.finlib.marketdata.MarketDataId;
import org.blacksmith.finlib.marketdata.MarketDataProvider;
import org.blacksmith.finlib.rate.marketdata.MarketDataWrapper;

public class MarketDataInMemoryProvider<I extends MarketDataId<V>, V extends MarketData> implements MarketDataProvider<I, V> {

  private final Map<I, List<MarketDataWrapper<I, V>>> marketData = new HashMap<>();

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
  public V getValue(I id, LocalDate date) {
    return marketData.getOrDefault(id, Collections.emptyList()).stream()
        .filter(m -> m.getValue().getDate().compareTo(date) <= 0)
        .max(MarketDataWrapper.marketDataDateComparator)
        .map(MarketDataWrapper::getValue)
        .orElse(null);
  }
}
