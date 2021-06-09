package org.blacksmith.finlib.curve.algorithm;

public class LinearInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;
  public LinearInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    AlgorithmUtils.checkMinSize(xValues, MIN_SIZE);
    AlgorithmUtils.checkArraysSize(yValues, xValues.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xValues.length, yValues.length));
    AlgorithmUtils.checkIncreasing(xValues, "X-values must increase");
    int n = xValues.length;
    final double[] af = new double[n];
    for (int i = 0; i < n - 1; i++) {
      af[i] = (yValues[i + 1] - yValues[i]) / (xValues[i + 1] - xValues[i]);
    }
    af[n - 1] = af[n - 2];
    double[] coefficients = new double[2];
    PolynomialFunction[] polynomials = new PolynomialFunction[xValues.length];
    for (int i = 0; i < xValues.length; i++) {
      coefficients[0] = yValues[i];
      coefficients[1] = af[i];
      polynomials[i] = new PolynomialFunction(coefficients);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }
}
