package org.blacksmith.finlib.rates.fxrates;

import java.time.LocalDate;
import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.basic.BasicMarketData;

public class FxRate1 extends BasicMarketData<FxRateId, Rate> implements FxRateOperations<FxRate1> {

  public FxRate1(LocalDate date, Rate rate) {
    super(date,rate);
  }

  public static FxRate1 of (LocalDate date, Rate rate) {
    return new FxRate1(date,rate);
  }

  @Override
  public FxRate1 inverse() {
      return new FxRate1(this.getDate(),this.getValue().inverse());
  }

  @Override
  public FxRate1 multiply(double multiplicand, int decimalPlaces) {
    return new FxRate1(this.getDate(),new Rate(this.getValue().getValue().doubleValue()*multiplicand,decimalPlaces));
  }

  @Override
  public FxRate1 divide(double divisor, int decimalPlaces) {
    return new FxRate1(this.getDate(),new Rate(this.getValue().getValue().doubleValue()/divisor,decimalPlaces));
  }

  @Override
  public FxRate1 crossDivide(double factor, FxRate1 divisor, int decimalPlaces) {
    return FxRate1.of(DateUtils.max(this.date,divisor.date),
        Rate.of(((factor*this.value.doubleValue()))/divisor.value.doubleValue(),decimalPlaces));
  }
}
