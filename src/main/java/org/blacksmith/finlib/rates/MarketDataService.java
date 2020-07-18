package org.blacksmith.finlib.rates;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public interface MarketDataService<K extends MarketDataId, V> {
  MarketData<K,V> getRate(K key, LocalDate date);

  default <R> R getRateValue(K key, LocalDate date, Function<MarketData<K,V>,R> valueExtractor) {
    return Optional.ofNullable(getRate(key,date))
        .map(valueExtractor)
        .orElse(null);
  }

  default V getRateValue(K key, LocalDate date) {
    return Optional.ofNullable(getRate(key,date))
        .map(MarketData::getValue)
        .orElse(null);
  }
}
