package org.blacksmith.finlib.rate.fxrate.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rate.fxrate.FxRate;

class FxRate1Internal implements FxRateOperations<FxRate1Internal> {
  private final LocalDate date;
  private final double value;

  public FxRate1Internal(LocalDate date, double value) {
    this.date = date;
    this.value = value;
  }

  public static FxRate1Internal of(LocalDate date, double value) {
    return new FxRate1Internal(date, value);
  }

  public FxRate toFxRate(int decimalPlaces) {
    return FxRate.of(date, Rate.of(value, decimalPlaces));
  }

  @Override
  public FxRate1Internal multiply(double multiplicand) {
    return new FxRate1Internal(this.date, this.value * multiplicand);
  }

  @Override
  public FxRate1Internal multiply(FxRate1Internal multiplicand) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), this.value * multiplicand.value);
  }

  @Override
  public FxRate1Internal divide(FxRate1Internal divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, divisor.date), this.value / divisor.value);
  }

  @Override
  public FxRate1Internal divide(double divisor) {
    return new FxRate1Internal(this.date, this.value / divisor);
  }

  @Override
  public FxRate1Internal inverse() {
    return new FxRate1Internal(this.date, 1 / this.value);
  }

  @Override
  public FxRate1Internal inverse2(double divisor) {
    return new FxRate1Internal(this.date, divisor / this.value);
  }

  @Override
  public FxRate1Internal inverse2(double numerator, FxRate1Internal multiplicand) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), numerator / (this.value * multiplicand.value));
  }

  @Override
  public FxRate1Internal multiplyAndDivide(FxRate1Internal multiplicand, double divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), this.value * multiplicand.value / divisor);
  }

  @Override
  public FxRate1Internal multiplyAndDivide(double multiplicand, FxRate1Internal divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, divisor.date), this.value * multiplicand / divisor.value);
  }
}
