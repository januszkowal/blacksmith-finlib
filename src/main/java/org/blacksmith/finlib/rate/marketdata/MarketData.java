package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;

public interface MarketData<V> {
  LocalDate getDate();

  V getValue();
}
