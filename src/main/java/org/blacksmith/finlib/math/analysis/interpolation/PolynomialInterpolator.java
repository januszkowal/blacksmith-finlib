package org.blacksmith.finlib.math.analysis.interpolation;

public interface PolynomialInterpolator {
  PolynomialSplineFunction interpolate(double[] xValues, double[] yValues);
}
