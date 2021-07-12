package org.blacksmith.finlib.rate.intrate;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.marketdata.BasicMarketData;

public class InterestRate extends BasicMarketData<Rate> {

  public InterestRate(Rate rate, LocalDate valueDate) {
    super(rate, valueDate);
  }

  public static InterestRate ofRate(Rate rate, LocalDate valueDate) {
    return new InterestRate(rate, valueDate);
  }
}
