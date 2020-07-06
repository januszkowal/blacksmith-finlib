package org.blacksmith.finlib.rates.fxrates;

public interface FxRateOperations<R> {
  R inverse();
  R multiply(double multiplicand, int decimalPlaces);
  R divide(double divisor, int decimalPlaces);
  R crossDivide(double factor, R divisor, int decimalPlaces);
}
