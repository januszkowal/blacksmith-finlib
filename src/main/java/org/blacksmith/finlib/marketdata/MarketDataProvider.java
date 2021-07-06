package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public interface MarketDataProvider<I extends MarketDataId<V>, V extends MarketData> {
  Optional<V> value(I id, LocalDate date);

  default <R> Optional<R> value(I id, LocalDate date, Function<V, R> valueExtractor) {
    return value(id, date)
        .map(valueExtractor);
  }
}
