package org.blacksmith.finlib.math.analysis.interpolation;

public class QuadraticSplineInterpolator extends AbstractPolynomialInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;

  public QuadraticSplineInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    validateKnots(xValues, yValues, MIN_SIZE);
    final int n = xValues.length - 1;
    // Differences between knot points
    final double[] h = new double[n];
    final double[] b = new double[n];
    for (int i = 0; i < n; i++) {
      h[i] = xValues[i + 1] - xValues[i];
      b[i] = (yValues[i + 1] - yValues[i]) / h[i];
    }

    final double u[] = new double[n];
    final double v[] = new double[n + 1];
    u[0] = 0.0;
    v[0] = 0.0;
    v[n] = 0.0;

    double g = 0;
    for (int i = 1; i < n; i++) {
      g = 2.0 * (h[i] + h[i - 1]) - h[i - 1] * u[i - 1];
      u[i] = h[i] / g;
      v[i] = (3.0 * (yValues[i + 1] * h[i - 1] - yValues[i] * (xValues[i + 1] - xValues[i - 1]) + yValues[i - 1] * h[i]) /
          (h[i - 1] * h[i]) - h[i - 1] * v[i - 1]) / g;
    }

    final double z[] = new double[n + 1];
    z[n] = 0d;

    for (int i = n - 1; i >= 0; i--) {
      z[i] = v[i] - u[i] * z[i + 1];
    }

    final PolynomialFunction polynomials[] = new PolynomialFunction[n];
    double coeffa, coeffb, coeffc, coeffd, hi;
    for (int i = 0; i < n; i++) {
      hi = h[i];
      coeffa = yValues[i];
      coeffb = -hi * (z[i + 1] + 2d * z[i]) / 3d + b[i];
      coeffc = z[i];
      coeffd = (z[i + 1] - z[i]) / (3.0d * hi);
      polynomials[i] = new PolynomialFunction(coeffa, coeffb, coeffc, coeffd);
    }

    return new PolynomialSplineFunction(xValues, polynomials);
  }
}
