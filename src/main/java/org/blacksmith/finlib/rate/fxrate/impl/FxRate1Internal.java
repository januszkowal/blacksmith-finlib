package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.fxrate.FxRate;

class FxRate1Internal implements FxRateOperations<FxRate1Internal> {
  private final LocalDate date;
  private final double value;

  public FxRate1Internal(double value, LocalDate date) {
    this.value = value;
    this.date = date;
  }

  public static FxRate1Internal of(double value, LocalDate date) {
    return new FxRate1Internal(value, date);
  }

  public FxRate toFxRate(int decimalPlaces) {
    return FxRate.of(Rate.of(value, decimalPlaces), date);
  }

  @Override
  public FxRate1Internal multiply(double multiplicand) {
    return new FxRate1Internal(this.value * multiplicand, this.date);
  }

  @Override
  public FxRate1Internal multiply(FxRate1Internal multiplicand) {
    return new FxRate1Internal(this.value * multiplicand.value, DateUtils.max(this.date, multiplicand.date));
  }

  @Override
  public FxRate1Internal divide(FxRate1Internal divisor) {
    return new FxRate1Internal(this.value / divisor.value, DateUtils.max(this.date, divisor.date));
  }

  @Override
  public FxRate1Internal divide(double divisor) {
    return new FxRate1Internal(this.value / divisor, this.date);
  }

  @Override
  public FxRate1Internal inverse() {
    return new FxRate1Internal(1 / this.value, this.date);
  }

  @Override
  public FxRate1Internal inverse2(double numerator) {
    return new FxRate1Internal(numerator / this.value, this.date);
  }

  @Override
  public FxRate1Internal multiplyAndDivide(FxRate1Internal multiplicand, double divisor) {
    return new FxRate1Internal(this.value * multiplicand.value / divisor, DateUtils.max(this.date, multiplicand.date));
  }

  @Override
  public FxRate1Internal multiplyAndDivide(double multiplicand, FxRate1Internal divisor) {
    return new FxRate1Internal(this.value * multiplicand / divisor.value, DateUtils.max(this.date, divisor.date));
  }

  @Override
  public FxRate1Internal multiplyAndInverse(FxRate1Internal multiplicand, double numerator) {
    return new FxRate1Internal(numerator / (this.value * multiplicand.value), DateUtils.max(this.date, multiplicand.date));
  }
}
