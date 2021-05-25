package org.blacksmith.finlib.rates.fxrates.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRate3;
import org.blacksmith.finlib.rates.fxrates.FxRate3RSource;

class FxRate3Internal implements FxRateOperations<FxRate3Internal> {
  private LocalDate date;
  private double buy;
  private double sell;
  private double avg;
  private int decimalPlaces;

  public FxRate3Internal(LocalDate date, double buy, double sell, double avg, int decimalPlaces) {
    this.date = date;
    this.buy = buy;
    this.sell = sell;
    this.avg = avg;
    this.decimalPlaces = decimalPlaces;
  }

  public static FxRate3Internal of(LocalDate date, FxRate3RSource.FxRate3RawValue value, int decimalPlaces) {
    return new FxRate3Internal(date, value.getBuy().doubleValue(), value.getSell().doubleValue(), value.getAvg().doubleValue(), decimalPlaces);
  }

  public static FxRate3Internal of(LocalDate date, double buy, double sell, double avg, int decimalPlaces) {
    return new FxRate3Internal(date, buy, sell, avg, decimalPlaces);
  }

  public FxRate3 toFxRate3() {
    return FxRate3.of(date, Rate.of(buy, decimalPlaces), Rate.of(sell, decimalPlaces), Rate.of(avg, decimalPlaces));
  }

  @Override
  public FxRate3Internal multiply(double multiplicand) {
    this.buy = this.buy * multiplicand;
    this.sell = this.sell * multiplicand;
    this.avg = this.avg * multiplicand;
    return this;
  }

  @Override
  public FxRate3Internal multiply(FxRate3Internal multiplicand) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.buy = this.buy * multiplicand.buy;
    this.sell = this.sell * multiplicand.sell;
    this.avg = this.avg * multiplicand.avg;
    return this;
  }

  public FxRate3Internal divide(double divisor) {
    this.buy = this.buy / divisor;
    this.sell = this.sell / divisor;
    this.avg = this.avg / divisor;
    return this;
  }

  @Override
  public FxRate3Internal inverse2(double divisor) {
    this.buy = divisor / this.buy;
    this.sell = divisor / this.sell;
    this.avg = divisor / this.avg;
    return this;
  }

  public FxRate3Internal divide(FxRate3Internal divisor) {
    this.date = DateUtils.max(this.date, divisor.date);
    this.buy = this.buy / divisor.buy;
    this.sell = this.sell / divisor.sell;
    this.avg = this.avg / divisor.avg;
    return this;
  }

  @Override
  public FxRate3Internal inverse() {
    this.buy = 1 / this.buy;
    this.sell = 1 / this.sell;
    this.avg = 1 / this.avg;
    return this;
  }

  @Override
  public FxRate3Internal multiplyAndDivide(double multiplicand, FxRate3Internal divisor) {
    this.date = DateUtils.max(this.date, divisor.date);
    this.buy = this.buy * multiplicand / divisor.buy;
    this.sell = this.sell * multiplicand / divisor.sell;
    this.avg = this.avg * multiplicand / divisor.avg;
    return this;
  }

  @Override
  public FxRate3Internal multiplyAndDivide(FxRate3Internal multiplicand, double divisor) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.buy = this.buy * multiplicand.buy / divisor;
    this.sell = this.sell * multiplicand.sell / divisor;
    this.avg = this.avg * multiplicand.avg / divisor;
    return this;
  }

  @Override
  public FxRate3Internal inverse2(double numerator, FxRate3Internal multiplicand) {
    this.date = DateUtils.max(this.date, multiplicand.date);
    this.buy = numerator / (this.buy * multiplicand.buy);
    this.sell = numerator / (this.sell * multiplicand.sell);
    this.avg = numerator / (this.avg * multiplicand.avg);
    return this;
  }
}
