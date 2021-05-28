package org.blacksmith.finlib.curve.algoritm;

public class AkimaSplineInterpolator implements PolynomialInterpolator {
  private final int MIN_SIZE = 3;

  public AkimaSplineInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xvals, double[] yvals) {
    AlgorithmUtils.checkMinSize(xvals, MIN_SIZE);
    AlgorithmUtils.checkArraysSize(yvals, xvals.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xvals.length, yvals.length));
    AlgorithmUtils.checkOrder(xvals, "X-values array must be in order");
    int n = xvals.length;
    /*
     * Shift data by+2 in the array and compute the secants
     * also calcualate extrapolated end point secants
     * */
    double[] secants = new double[n + 3];
    for (int i = 0; i < n - 1; i++) {
      secants[i + 2] = (yvals[i + 1] - yvals[i]) / (xvals[i + 1] - xvals[i]);
    }
    secants[1] = 2 * secants[2] - secants[3];
    secants[0] = 2 * secants[1] - secants[2];
    secants[n + 1] = 2 * secants[n] - secants[n - 1];
    secants[n + 2] = 2 * secants[n + 1] - secants[n];
    /*
     * Compute slopes
     * */
    double a, b;
    double[] firstDerivatives = new double[xvals.length];
    for (int i = 0; i < xvals.length; i++) {
      a = Math.abs(secants[i + 3] - secants[i + 2]);
      b = Math.abs(secants[i + 1] - secants[i]);
      if ((a + b) != 0) {
        firstDerivatives[i] = (a * secants[i + 1] + b * secants[i + 2]) / (a + b);
      } else {
        firstDerivatives[i] = 0.5 * (secants[i + 2] + secants[i + 1]);
      }
    }
    PolynomialSplineFunction.Polynomial[] polynomials = new PolynomialSplineFunction.Polynomial[n - 1];
    double coefficients[] = new double[4];
    double xDelta;
    for (int i = 0; i < n - 1; i++) {
      xDelta = xvals[i + 1] - xvals[i];
      coefficients[0] = yvals[i];
      coefficients[1] = firstDerivatives[i];
      coefficients[2] = (3.0d * secants[i + 2] - 2.0d * firstDerivatives[i] - firstDerivatives[i + 1]) / xDelta;
      coefficients[3] = (-2.0d * secants[i + 2] + firstDerivatives[i] + firstDerivatives[i + 1]) / (xDelta * xDelta);
      polynomials[i] = new PolynomialSplineFunction.Polynomial(coefficients);
    }
    return new PolynomialSplineFunction(xvals, polynomials);
  }
}
