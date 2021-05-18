package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.basic.BasicMarketData;

public class FxRate extends BasicMarketData<Rate> implements FxRateOperations<FxRate> {

  public FxRate(LocalDate date, Rate rate) {
    super(date, rate);
  }

  public static FxRate of(LocalDate date, Rate rate) {
    return new FxRate(date, rate);
  }

  @Override
  public FxRate inverse() {
    return new FxRate(this.getDate(), this.value.inverse());
  }

  @Override
  public FxRate add(double augend, int decimalPlaces) {
    return new FxRate(this.getDate(), this.value.add(augend, decimalPlaces));
  }

  @Override
  public FxRate add(double augend) {
    return new FxRate(this.getDate(), this.value.add(augend));
  }

  @Override
  public FxRate add(BigDecimal augend, int decimalPlaces) {
    return new FxRate(this.getDate(), this.value.add(augend, decimalPlaces));
  }

  @Override
  public FxRate add(BigDecimal augend) {
    return new FxRate(this.getDate(), this.value.add(augend));
  }

  @Override
  public FxRate multiply(double multiplicand, int decimalPlaces) {
    return new FxRate(this.getDate(), new Rate(this.value.multiply(multiplicand, decimalPlaces)));
  }

  @Override
  public FxRate multiply(double multiplicand) {
    return new FxRate(this.getDate(), new Rate(this.value.multiply(multiplicand)));
  }

  @Override
  public FxRate multiply(BigDecimal multiplicand, int decimalPlaces) {
    return new FxRate(this.getDate(), new Rate(this.value.multiply(multiplicand, decimalPlaces)));
  }

  @Override
  public FxRate multiply(BigDecimal multiplicand) {
    return new FxRate(this.getDate(), new Rate(this.value.multiply(multiplicand)));
  }

  @Override
  public FxRate divide(double divisor, int decimalPlaces) {
    return new FxRate(this.getDate(), new Rate(this.value.divide(divisor, decimalPlaces)));
  }

  @Override
  public FxRate divide(double divisor) {
    return new FxRate(this.getDate(), new Rate(this.value.divide(divisor)));
  }

  @Override
  public FxRate divide(BigDecimal divisor, int decimalPlaces) {
    return new FxRate(this.getDate(), new Rate(this.value.divide(divisor, decimalPlaces)));
  }

  @Override
  public FxRate divide(BigDecimal divisor) {
    return new FxRate(this.getDate(), new Rate(this.value.divide(divisor)));
  }

  @Override
  public FxRate crossDivide(double factor, FxRate divisor, int decimalPlaces) {
    return FxRate.of(DateUtils.max(this.date, divisor.date),
        Rate.of(((factor * this.value.doubleValue())) / divisor.value.doubleValue(), decimalPlaces));
  }

  @Override
  public FxRate crossDivide(BigDecimal factor, FxRate divisor, int decimalPlaces) {
    return FxRate.of(DateUtils.max(this.date, divisor.date),
        Rate.of(((factor.doubleValue() * this.value.doubleValue())) / divisor.value.doubleValue(), decimalPlaces));
  }

  @Override
  public FxRate crossDivide(double factor, FxRate divisor) {
    return crossDivide(factor, divisor, value.decimalPlaces());
  }

  @Override
  public FxRate crossDivide(BigDecimal factor, FxRate divisor) {
    return crossDivide(factor, divisor, value.decimalPlaces());
  }
}
