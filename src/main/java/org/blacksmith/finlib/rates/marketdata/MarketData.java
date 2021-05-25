package org.blacksmith.finlib.rates.marketdata;

import java.time.LocalDate;

public interface MarketData<V> {
  LocalDate getDate();

  V getValue();
}
