package org.blacksmith.finlib.math.analysis.interpolation;

public class AkimaSplineInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 3;

  public AkimaSplineInterpolator() {
  }

  @Override
  public InterpolatedFunction interpolate(double[] xValues, double[] yValues) {
    InterpolationUtils.checkMinSize(xValues, MIN_SIZE);
    InterpolationUtils.checkArraysSize(yValues, xValues.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xValues.length, yValues.length));
    InterpolationUtils.checkIncreasing(xValues, "X-values must increase");
    double[] secants = calculateSecants(xValues, yValues);
    double[] firstDerivatives = calculateFirstDerivatives(secants, xValues.length);

    int intervals = xValues.length - 1;
    PolynomialFunction[] polynomials = new PolynomialFunction[intervals];
    for (int i = 0; i < intervals; i++) {
      polynomials[i] = polynomial(xValues, yValues, firstDerivatives, secants, i);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(double[] xValues, double[] yValues, double[] firstDerivatives, double[] secants, int index) {
    double xDelta = xValues[index + 1] - xValues[index];
    double a = yValues[index];
    double b = firstDerivatives[index];
    double c = (3.0d * secants[index + 2] - 2.0d * firstDerivatives[index] - firstDerivatives[index + 1]) / xDelta;
    double d = (-2.0d * secants[index + 2] + firstDerivatives[index] + firstDerivatives[index + 1]) / (xDelta * xDelta);
    return new PolynomialFunction(a, b, c, d);
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

  private double[] calculateFirstDerivatives(double[] secants, int count) {
    double a, b;
    double[] firstDerivatives = new double[count];
    for (int i = 0; i < count; i++) {
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
