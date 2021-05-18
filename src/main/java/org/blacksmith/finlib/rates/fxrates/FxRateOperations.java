package org.blacksmith.finlib.rates.fxrates;

import java.math.BigDecimal;

public interface FxRateOperations<R> {
  R inverse();
  R add(double augend, int decimalPlaces);
  R add(double augend);
  R add(BigDecimal augend, int decimalPlaces);
  R add(BigDecimal augend);
  R multiply(double multiplicand, int decimalPlaces);
  R multiply(double multiplicand);
  R multiply(BigDecimal multiplicand, int decimalPlaces);
  R multiply(BigDecimal multiplicand);
  R divide(double divisor, int decimalPlaces);
  R divide(double divisor);
  R divide(BigDecimal divisor, int decimalPlaces);
  R divide(BigDecimal divisor);
  R crossDivide(double factor, R divisor, int decimalPlaces);
  R crossDivide(BigDecimal factor, R divisor, int decimalPlaces);
  R crossDivide(double factor, R divisor);
  R crossDivide(BigDecimal factor, R divisor);
}
