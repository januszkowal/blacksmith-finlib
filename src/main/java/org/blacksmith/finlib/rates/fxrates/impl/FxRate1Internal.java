package org.blacksmith.finlib.rates.fxrates.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRate;

class FxRate1Internal implements FxRateOperations<FxRate1Internal> {
  private final LocalDate date;
  private final double value;
  private final int decimalPlaces;

  public FxRate1Internal(LocalDate date, double value, int decimalPlaces) {
    this.date = date;
    this.value = value;
    this.decimalPlaces = decimalPlaces;
  }

  public static FxRate1Internal of(LocalDate date, double value, int decimalPlaces) {
    return new FxRate1Internal(date, value, decimalPlaces);
  }

  public FxRate toFxRate() {
    return FxRate.of(date, Rate.of(value, decimalPlaces));
  }

  @Override
  public FxRate1Internal multiply(double multiplicand) {
    return new FxRate1Internal(this.date, this.value * multiplicand, decimalPlaces);
  }

  @Override
  public FxRate1Internal multiply(FxRate1Internal multiplicand) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), this.value * multiplicand.value, decimalPlaces);
  }

  @Override
  public FxRate1Internal divide(FxRate1Internal divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, divisor.date), this.value / divisor.value, this.decimalPlaces);
  }

  @Override
  public FxRate1Internal divide(double divisor) {
    return new FxRate1Internal(this.date, this.value / divisor, this.decimalPlaces);
  }

  @Override
  public FxRate1Internal inverse() {
    return new FxRate1Internal(this.date, 1 / this.value, this.decimalPlaces);
  }

  @Override
  public FxRate1Internal inverse2(double divisor) {
    return new FxRate1Internal(this.date, divisor / this.value, decimalPlaces);
  }

  @Override
  public FxRate1Internal inverse2(double numerator, FxRate1Internal multiplicand) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), numerator / (this.value * multiplicand.value),
        this.decimalPlaces);
  }

  @Override
  public FxRate1Internal multiplyAndDivide(FxRate1Internal multiplicand, double divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, multiplicand.date), this.value * multiplicand.value / divisor, this.decimalPlaces);
  }

  @Override
  public FxRate1Internal multiplyAndDivide(double multiplicand, FxRate1Internal divisor) {
    return new FxRate1Internal(DateUtils.max(this.date, divisor.date), this.value * multiplicand / divisor.value, this.decimalPlaces);
  }
}
