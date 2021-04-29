package org.blacksmith.finlib.rates.basic;

import java.time.LocalDate;
import lombok.ToString;
import org.blacksmith.finlib.rates.MarketData;

@ToString
public class BasicMarketData<V> implements MarketData<V> {

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

  public static <V> BasicMarketData<V> of(LocalDate date, V value) {
    return new BasicMarketData<>(date,value);
  }
}