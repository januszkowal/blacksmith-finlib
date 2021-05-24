package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.MarketData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

@Getter
@ToString
@AllArgsConstructor
public class FxRate3Raw implements MarketData<FxRate3Raw.FxRateRaw3Data> {
  private final LocalDate date;
  private final FxRateRaw3Data value;

  public static FxRate3Raw of(LocalDate date, Rate buy, Rate sell, Rate avg) {
    return new FxRate3Raw(date, FxRateRaw3Data.of(buy, sell, avg));
  }

  public static FxRate3Raw of(LocalDate date, BigDecimal buy, BigDecimal sell, BigDecimal avg, int decimalPlaces) {
    return new FxRate3Raw(date, FxRateRaw3Data.of(Rate.of(buy, decimalPlaces), Rate.of(sell, decimalPlaces), Rate.of(avg)));
  }

  public static FxRate3Raw of(LocalDate date, double buyRate, double sellRate, double avgRate, int decimalPlaces) {
    return new FxRate3Raw(date, FxRateRaw3Data
        .of(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces), Rate.of(avgRate, decimalPlaces)));
  }

  @Value(staticConstructor = "of")
  public static class FxRateRaw3Data {
    Rate buy;
    Rate sell;
    Rate avg;
  }
}
