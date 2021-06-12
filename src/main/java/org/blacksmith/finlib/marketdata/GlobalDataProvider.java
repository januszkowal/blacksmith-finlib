package org.blacksmith.finlib.marketdata;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public interface GlobalDataProvider {
  <I extends MarketDataId<V>, V extends MarketData> V getValue(I id, LocalDate date);
}
