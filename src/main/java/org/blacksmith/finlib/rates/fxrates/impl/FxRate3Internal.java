package org.blacksmith.finlib.rates.fxrates.impl;

import java.time.LocalDate;

import org.blacksmith.commons.datetime.DateUtils;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.rates.fxrates.FxRate3;
import org.blacksmith.finlib.rates.fxrates.FxRate3RSource;

class FxRate3Internal implements FxRateOperations<FxRate3Internal> {
  private final LocalDate date;
  private final double buy;
  private final double sell;
  private final double avg;
  private final int decimalPlaces;

  public FxRate3Internal(LocalDate date, double buy, double sell, double avg, int decimalPlaces) {
    this.date = date;
    this.buy = buy;
    this.sell = sell;
    this.avg = avg;
    this.decimalPlaces = decimalPlaces;
  }

  public static FxRate3Internal of(LocalDate date, FxRate3RSource.FxRate3RawValue value, int decimalPlaces) {
    return new FxRate3Internal(date, value.getBuy().doubleValue(), value.getSell().doubleValue(), value.getAvg().doubleValue(),
        decimalPlaces);
  }

  public static FxRate3Internal of(LocalDate date, double buy, double sell, double avg, int decimalPlaces) {
    return new FxRate3Internal(date, buy, sell, avg, decimalPlaces);
  }

  public FxRate3 toFxRate3() {
    return FxRate3.of(date, Rate.of(buy, decimalPlaces), Rate.of(sell, decimalPlaces), Rate.of(avg, decimalPlaces));
  }

  @Override
  public FxRate3Internal multiply(double multiplicand) {
    return new FxRate3Internal(this.date,
        this.buy * multiplicand,
        this.sell * multiplicand,
        this.avg * multiplicand, this.decimalPlaces);
  }

  @Override
  public FxRate3Internal multiply(FxRate3Internal multiplicand) {
    return new FxRate3Internal(DateUtils.max(this.date, multiplicand.date),
        this.buy * multiplicand.buy,
        this.sell * multiplicand.sell,
        this.avg * multiplicand.avg,
        this.decimalPlaces);
  }

  public FxRate3Internal divide(FxRate3Internal divisor) {
    return new FxRate3Internal(DateUtils.max(this.date, divisor.date),
        this.buy / divisor.buy,
        this.sell / divisor.sell,
        this.avg / divisor.avg,
        this.decimalPlaces);
  }

  public FxRate3Internal divide(double divisor) {
    return new FxRate3Internal(this.date,
        this.buy / divisor,
        this.sell / divisor,
        this.avg / divisor,
        this.decimalPlaces);
  }

  @Override
  public FxRate3Internal inverse() {
    return new FxRate3Internal(this.date,
        1 / this.buy,
        1 / this.sell,
        1 / this.avg,
        this.decimalPlaces);
  }

  @Override
  public FxRate3Internal inverse2(double divisor) {
    return new FxRate3Internal(this.date,
        divisor / this.buy,
        divisor / this.sell,
        divisor / this.avg,
        this.decimalPlaces);
  }

  @Override
  public FxRate3Internal inverse2(double numerator, FxRate3Internal multiplicand) {
    return new FxRate3Internal(DateUtils.max(this.date, multiplicand.date),
        numerator / (this.buy * multiplicand.buy),
        numerator / (this.sell * multiplicand.sell),
        numerator / (this.avg * multiplicand.avg),
        this.decimalPlaces);
  }

  @Override
  public FxRate3Internal multiplyAndDivide(FxRate3Internal multiplicand, double divisor) {
    return new FxRate3Internal(DateUtils.max(this.date, multiplicand.date),
        this.buy * multiplicand.buy / divisor,
        this.sell * multiplicand.sell / divisor,
        this.avg * multiplicand.avg / divisor,
        this.decimalPlaces);
  }

  @Override
  public FxRate3Internal multiplyAndDivide(double multiplicand, FxRate3Internal divisor) {
    return new FxRate3Internal(DateUtils.max(this.date, divisor.date),
        this.buy * multiplicand / divisor.buy,
        this.sell * multiplicand / divisor.sell,
        this.avg * multiplicand / divisor.avg,
        this.decimalPlaces);
  }
}
