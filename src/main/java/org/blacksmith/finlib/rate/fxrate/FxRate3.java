package org.blacksmith.finlib.rate.fxrate;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.BasicMarketData;

import lombok.Value;

public class FxRate3 extends BasicMarketData<FxRate3.FxRate3Data> {

  public FxRate3(LocalDate date, FxRate3.FxRate3Data rate3Values) {
    super(date, rate3Values);
  }

  public static FxRate3 of(LocalDate date, Rate buy, Rate sell, Rate avg) {
    return new FxRate3(date, FxRate3.FxRate3Data.of(buy, sell, avg));
  }

  @Value(staticConstructor = "of")
  public static class FxRate3Data {
    Rate buy;
    Rate sell;
    Rate avg;
  }
}
