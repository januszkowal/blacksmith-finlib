package org.blacksmith.finlib.rate.fxrate;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.BasicMarketData;

public class FxRate extends BasicMarketData<Rate> {

  public FxRate(LocalDate date, Rate rate) {
    super(date, rate);
  }

  public static FxRate of(LocalDate date, Rate rate) {
    return new FxRate(date, rate);
  }
}
