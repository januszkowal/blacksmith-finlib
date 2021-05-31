package org.blacksmith.finlib.curve.algorithm;

public interface PolynomialInterpolator {
  PolynomialFunction interpolate(double[] xVals, double[] yVals);
}
