package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Comparator;

public interface MarketDataWrapper<I extends MarketDataId<M>, M extends MarketData> {
  Comparator<MarketDataWrapper<?, ?>> marketDataDateComparator =
      Comparator.comparing(m -> m.getValue().getDate());

  I getId();

  M getValue();

  default LocalDate getDate() {
    return getValue().getDate();
  }
}
