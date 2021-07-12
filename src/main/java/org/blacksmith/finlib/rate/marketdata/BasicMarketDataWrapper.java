package org.blacksmith.finlib.rate.marketdata;

import org.blacksmith.finlib.marketdata.MarketData;
import org.blacksmith.finlib.marketdata.MarketDataId;
import org.blacksmith.finlib.marketdata.MarketDataWrapper;

import lombok.ToString;

@ToString
public class BasicMarketDataWrapper<I extends MarketDataId<V>, V extends MarketData> implements MarketDataWrapper<I, V> {

  private final I id;
  private final V value;

  public BasicMarketDataWrapper(I id, V value) {
    this.id = id;
    this.value = value;
  }

  public static <I extends MarketDataId<V>, V extends MarketData> BasicMarketDataWrapper<I, V> of(I id, V value) {
    return new BasicMarketDataWrapper<>(id, value);
  }

  @Override
  public I getId() {
    return this.id;
  }

  @Override
  public V getValue() {
    return this.value;
  }
}
