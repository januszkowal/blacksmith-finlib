package org.blacksmith.finlib.rates.fxrates.internal;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class FxRate1Internal implements FxRateOperations<FxRate1Internal> {
  private LocalDate date;
  private double value;
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
    this.value = this.value * multiplicand;
    return this;
  }

  @Override
  public FxRate1Internal multiply(FxRate1Internal multiplicand) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.value = this.value * multiplicand.value;
    return this;
  }

  @Override
  public FxRate1Internal divide(double divisor) {
    this.value = this.value / divisor;
    return this;
  }

  @Override
  public FxRate1Internal divide(FxRate1Internal divisor) {
    this.date = DateUtils.max(this.date, divisor.date);
    this.value = this.value / divisor.value;
    return this;
  }

  @Override
  public FxRate1Internal inverse2(double divisor) {
    this.value = divisor / this.value;
    return this;
  }

  @Override
  public FxRate1Internal inverse() {
    this.value = 1 / this.value;
    return this;
  }

  @Override
  public FxRate1Internal multiplyAndDivide(double multiplicand, FxRate1Internal divisor) {
    this.date = DateUtils.max(this.date, divisor.date);
    this.value = this.value * multiplicand / divisor.value;
    return this;
  }

  @Override
  public FxRate1Internal multiplyAndDivide(FxRate1Internal multiplicand, double divisor) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.value = this.value * multiplicand.value / divisor;
    return this;
  }

  @Override
  public FxRate1Internal inverse2(double numerator, FxRate1Internal multiplicand) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.value = numerator / (this.value * multiplicand.value);
    return this;
  }
}
