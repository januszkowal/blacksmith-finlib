package org.blacksmith.finlib.rates.basic;

import java.time.LocalDate;
import lombok.ToString;
import org.blacksmith.finlib.rates.MarketData;

@ToString
public abstract class BasicMarketData<K,V> implements MarketData<K,V> {

  protected final LocalDate date;
  protected final V value;

  public BasicMarketData(LocalDate date, V value) {
    this.date = date;
    this.value = value;
  }

  @Override
  public LocalDate getDate() {
    return this.date;
  }

  @Override
  public V getValue() {
    return this.value;
  }
}
