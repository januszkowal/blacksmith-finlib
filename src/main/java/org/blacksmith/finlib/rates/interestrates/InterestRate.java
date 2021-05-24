package org.blacksmith.finlib.rates.interestrates;

import java.time.LocalDate;

import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.BasicMarketData;

public class InterestRate extends BasicMarketData<Rate> {

  public InterestRate(LocalDate valueDate, Rate rate) {
    super(valueDate, rate);
  }

  public static InterestRate of(LocalDate valueDate, Rate rate) {
    return new InterestRate(valueDate, rate);
  }
}
