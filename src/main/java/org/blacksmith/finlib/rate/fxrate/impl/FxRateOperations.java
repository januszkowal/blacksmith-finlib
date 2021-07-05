package org.blacksmith.finlib.rate.fxrate.impl;

interface FxRateOperations<R> {
  R multiply(double multiplicand);
  R multiply(R multiplicand);
  R divide(R divisor);
  R divide(double divisor);
  R inverse();
  R inverse2(double numerator);
  R multiplyAndDivide(R multiplicand, double divisor);
  R multiplyAndDivide(double multiplicand, R divisor);
  R multiplyAndInverse(R multiplicand, double numerator);
}
