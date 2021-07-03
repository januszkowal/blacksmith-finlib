package org.blacksmith.finlib.rate.fxrate;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.BasicMarketData;

import lombok.Value;

public class FxRate3 extends BasicMarketData<FxRate3.Data> {

  public FxRate3(FxRate3.Data rate3Values, LocalDate date) {
    super(rate3Values, date);
  }

  public static FxRate3 of(LocalDate date, Rate buy, Rate sell, Rate avg) {
    return new FxRate3(FxRate3.Data.of(buy, sell, avg), date);
  }

  public static FxRate3 of(double buyRate, double sellRate, double avgRate, int decimalPlaces, LocalDate date) {
    return new FxRate3(FxRate3.Data.of(Rate.of(buyRate, decimalPlaces), Rate.of(sellRate, decimalPlaces), Rate.of(avgRate, decimalPlaces)), date);
  }

  @Value(staticConstructor = "of")
  public static class Data {
    Rate buy;
    Rate sell;
    Rate avg;
  }
}
