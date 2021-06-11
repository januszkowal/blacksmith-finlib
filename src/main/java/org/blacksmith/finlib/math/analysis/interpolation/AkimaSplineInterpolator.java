package org.blacksmith.finlib.math.analysis.interpolation;

public class AkimaSplineInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 3;

  public AkimaSplineInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    InterpolationUtils.checkMinSize(xValues, MIN_SIZE);
    InterpolationUtils.checkArraysSize(yValues, xValues.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xValues.length, yValues.length));
    InterpolationUtils.checkIncreasing(xValues, "X-values must increase");
    int n = xValues.length;
    double[] secants = calculateSecants(xValues, yValues);
    double[] firstDerivatives = calculateFirstDerivatives(xValues, yValues, secants);

    PolynomialFunction[] polynomials = new PolynomialFunction[n - 1];
    double[] coefficients = new double[4];
    double xDelta;
    for (int i = 0; i < n - 1; i++) {
      xDelta = xValues[i + 1] - xValues[i];
      coefficients[0] = yValues[i];
      coefficients[1] = firstDerivatives[i];
      coefficients[2] = (3.0d * secants[i + 2] - 2.0d * firstDerivatives[i] - firstDerivatives[i + 1]) / xDelta;
      coefficients[3] = (-2.0d * secants[i + 2] + firstDerivatives[i] + firstDerivatives[i + 1]) / (xDelta * xDelta);
      polynomials[i] = new PolynomialFunction(coefficients);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private double[] calculateSecants(double[] xValues, double[] yValues) {
    /*
     * Shift data by+2 in the array and compute the secants
     * also calculate extrapolated end point secants
     * */
    int n = xValues.length;
    double[] secants = new double[n + 3];
    for (int i = 0; i < n - 1; i++) {
      secants[i + 2] = (yValues[i + 1] - yValues[i]) / (xValues[i + 1] - xValues[i]);
    }
    secants[1] = 2 * secants[2] - secants[3];
    secants[0] = 2 * secants[1] - secants[2];
    secants[n + 1] = 2 * secants[n] - secants[n - 1];
    secants[n + 2] = 2 * secants[n + 1] - secants[n];
    return secants;
  }

  private double[] calculateFirstDerivatives(double[] xValues, double[] yValues, double[] secants) {
    /*
     * Compute slopes
     * */
    double a, b;
    double[] firstDerivatives = new double[xValues.length];
    for (int i = 0; i < xValues.length; i++) {
      a = Math.abs(secants[i + 3] - secants[i + 2]);
      b = Math.abs(secants[i + 1] - secants[i]);
      if ((a + b) != 0) {
        firstDerivatives[i] = (a * secants[i + 1] + b * secants[i + 2]) / (a + b);
      } else {
        firstDerivatives[i] = 0.5 * (secants[i + 2] + secants[i + 1]);
      }
    }
    return firstDerivatives;
  }
}