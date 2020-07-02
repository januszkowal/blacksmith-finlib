package org.blacksmith.finlib.rates.interestrates;

import java.time.LocalDate;
import org.blacksmith.finlib.basic.Rate;
import org.blacksmith.finlib.rates.basic.BasicMarketData;

public class InterestRate extends BasicMarketData<InterestRateId,Rate> {

  public InterestRate(LocalDate valueDate, Rate rate) {
    super(valueDate,rate);
  }

  public static InterestRate of(LocalDate valueDate, Rate rate) {
    return new InterestRate(valueDate,rate);
  }
}
