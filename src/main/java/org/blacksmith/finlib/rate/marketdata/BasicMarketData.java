package org.blacksmith.finlib.rate.marketdata;

import java.time.LocalDate;

import org.blacksmith.finlib.marketdata.MarketData;

import lombok.ToString;

@ToString
public class BasicMarketData<V> implements MarketData {

  @ToString.Include
  protected final V value;
  @ToString.Include
  protected final LocalDate date;

  public BasicMarketData(V value, LocalDate date) {
    this.value = value;
    this.date = date;
  }

  public static <V> BasicMarketData<V> of(LocalDate date, V value) {
    return new BasicMarketData<>(value, date);
  }

  public V getValue() {
    return this.value;
  }

  @Override
  public LocalDate getDate() {
    return this.date;
  }
}
