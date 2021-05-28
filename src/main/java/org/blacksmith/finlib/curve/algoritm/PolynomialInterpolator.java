package org.blacksmith.finlib.curve.algoritm;

public interface PolynomialInterpolator {
  PolynomialFunction interpolate(double[] xvals, double[] yvals);
}
