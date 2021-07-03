package org.blacksmith.finlib.rate.fxrate;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.BasicMarketData;

public class FxRate extends BasicMarketData<Rate> {

  public FxRate(Rate rate, LocalDate date) {
    super(rate, date);
  }

  public static FxRate of(Rate rate, LocalDate date) {
    return new FxRate(rate, date);
  }
}
