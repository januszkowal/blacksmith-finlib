package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;
import java.util.Comparator;

import org.blacksmith.finlib.marketdata.MarketData;
import org.blacksmith.finlib.marketdata.MarketDataId;

public interface MarketDataWrapper<I extends MarketDataId<M>, M extends MarketData> {
  Comparator<MarketDataWrapper<?, ?>> marketDataDateComparator =
      Comparator.comparing(m -> m.getValue().getDate());

  I getId();

  M getValue();

  default LocalDate getDate() {
    return getValue().getDate();
  }
}
