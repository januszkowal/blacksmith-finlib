package org.blacksmith.finlib.math.analysis.interpolation;

public interface PolynomialInterpolator {
  InterpolatedFunction interpolate(double[] xValues, double[] yValues);
}
