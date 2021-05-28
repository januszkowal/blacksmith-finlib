package org.blacksmith.finlib.curve.algorithm;

public interface PolynomialInterpolator {
  PolynomialFunction interpolate(double[] xvals, double[] yvals);
}
