package org.blacksmith.finlib.curve.algorithm;

public class LinearInterpolator implements PolynomialInterpolator {
  private final int MIN_SIZE = 2;
  public LinearInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xVals, double[] yVals) {
    AlgorithmUtils.checkMinSize(xVals, MIN_SIZE);
    AlgorithmUtils.checkArraysSize(yVals, xVals.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xVals.length, yVals.length));
    AlgorithmUtils.checkIncreasing(xVals, "X-values must increase");
    int n = xVals.length;
    final double[] af = new double[n];
    for (int i = 0; i < n - 1; i++) {
      af[i] = (yVals[i + 1] - yVals[i]) / (xVals[i + 1] - xVals[i]);
    }
    af[n - 1] = af[n - 2];
    double[] coefficients = new double[2];
    PolynomialSplineFunction.Polynomial[] polynominals = new PolynomialSplineFunction.Polynomial[xVals.length];
    for (int i = 0; i < xVals.length; i++) {
      coefficients[0] = yVals[i];
      coefficients[1] = af[i];
      polynominals[i] = new PolynomialSplineFunction.Polynomial(coefficients);
    }
    return new PolynomialSplineFunction(xVals, polynominals);
  }
}
