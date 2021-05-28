package org.blacksmith.finlib.curves.algoritm;

public interface PolynomialInterpolator {
  PolynomialFunction interpolate(double[] xvals, double[] yvals);
}
