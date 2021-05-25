package org.blacksmith.finlib.rates.fxrates.impl;

interface FxRateOperations<R> {
  R multiply(double multiplicand);
  R multiply(R multiplicand);
  R divide(R divisor);
  R divide(double divisor);
  R inverse();
  R inverse2(double numerator);
  R inverse2(double numerator, R multiplicand);
  R multiplyAndDivide(R multiplicand, double divisor);
  R multiplyAndDivide(double multiplicand, R divisor);
}
