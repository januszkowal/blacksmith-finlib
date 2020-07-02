package org.blacksmith.finlib.rates.fxrates;

public interface FxRateOperatoins<R> {
  R inverse();
  R multiply(double multiplicand, int decimalPlaces);
  R crossDivide(double factor, R divisor, int decimalPlaces);
}
