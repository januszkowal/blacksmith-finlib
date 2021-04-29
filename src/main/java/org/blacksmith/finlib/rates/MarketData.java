package org.blacksmith.finlib.rates;

import java.time.LocalDate;

public interface MarketData<V> {
  LocalDate getDate();
  V getValue();
}