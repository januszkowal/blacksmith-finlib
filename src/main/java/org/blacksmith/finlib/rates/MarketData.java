package org.blacksmith.finlib.rates;

import java.time.LocalDate;

public interface MarketData<K,V> {
  LocalDate getDate();
  V getValue();
}
