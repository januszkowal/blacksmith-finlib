package org.blacksmith.finlib.math.analysis.interpolation;

import org.blacksmith.commons.arg.ArgChecker;

public abstract class AbstractPolynomialInterpolator {
  public void validateKnots(double[] xValues, double[] yValues, int minSize) {
    ArgChecker.notNull(xValues, "X-values array can't be null");
    ArgChecker.notNull(yValues, "Y-values array can't be null");
    InterpolationUtils.checkMinSize(xValues, minSize);
    InterpolationUtils.checkArraysSize(yValues, xValues.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xValues.length,
            yValues.length));
    InterpolationUtils.checkValidNumbers(xValues, "X-values must be valid numbers");
    InterpolationUtils.checkValidNumbers(yValues, "Y-values must be valid numbers");
    InterpolationUtils.checkIncreasing(xValues, "X-values must increase");
  }

  public double[] hcoeffs(double[] xValues) {
    int n = xValues.length - 1;
    final double h[] = new double[n];
    for (int i = 0; i < n; i++) {
      h[i] = xValues[i + 1] - xValues[i];
    }
    return h;
  }
}
