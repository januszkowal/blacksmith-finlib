package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public interface MarketDataProvider<I extends MarketDataId<V>, V extends MarketData> {
  V getValue(I id, LocalDate date);

  default <R> R getValue(I id, LocalDate date, Function<V, R> valueExtractor) {
    return Optional.ofNullable(getValue(id, date))
        .map(valueExtractor)
        .orElse(null);
  }
}
