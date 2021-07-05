package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public interface MarketDataProvider<I extends MarketDataId<V>, V extends MarketData> {
  V get(I id, LocalDate date);

  default <R> R get(I id, LocalDate date, Function<V, R> valueExtractor) {
    return Optional.ofNullable(get(id, date))
        .map(valueExtractor)
        .orElse(null);
  }
}
