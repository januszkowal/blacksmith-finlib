package org.blacksmith.finlib.curve.algorithm;

public interface PolynomialInterpolator {
  PolynomialSplineFunction interpolate(double[] xValues, double[] yValues);
}
