package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;

import lombok.ToString;

@ToString
public class BasicMarketData<V> implements MarketData<V> {

  @ToString.Include
  protected final LocalDate date;
  @ToString.Include
  protected final V value;

  public BasicMarketData(LocalDate date, V value) {
    this.date = date;
    this.value = value;
  }

  public static <V> BasicMarketData<V> of(LocalDate date, V value) {
    return new BasicMarketData<>(date, value);
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
