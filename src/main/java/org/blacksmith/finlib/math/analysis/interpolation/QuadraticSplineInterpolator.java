package org.blacksmith.finlib.math.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathArrays;

public class QuadraticSplineInterpolator extends AbstractPolynomialInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;

  public QuadraticSplineInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    validateKnots(xValues, yValues, MIN_SIZE);
    final int n = xValues.length - 1;
    // Differences between knot points
    final double h[] = new double[n];
    for (int i = 0; i < n; i++) {
      h[i] = xValues[i + 1] - xValues[i];
    }

    final double mu[] = new double[n];
    final double z[] = new double[n + 1];
    mu[0] = 0d;
    z[0] = 0d;
    z[n] = 0d;

    double g = 0;
    for (int i = 1; i < n; i++) {
      g = 2d * (xValues[i+1]  - xValues[i - 1]) - h[i - 1] * mu[i -1];
      mu[i] = h[i] / g;
      z[i] = (3d * (yValues[i + 1] * h[i - 1] - yValues[i] * (xValues[i + 1] - xValues[i - 1])+ yValues[i - 1] * h[i]) /
          (h[i - 1] * h[i]) - h[i - 1] * z[i - 1]) / g;
    }

    final double b[] = new double[n];
    final double c[] = new double[n + 1];
    final double d[] = new double[n];
    c[n] = 0d;

    for (int j = n -1; j >=0; j--) {
      c[j] = z[j] - mu[j] * c[j + 1];
      b[j] = (yValues[j + 1] - yValues[j]) / h[j] - h[j] * (c[j + 1] + 2d * c[j]) / 3d;
      d[j] = (c[j + 1] - c[j]) / (3d * h[j]);
    }

    final PolynomialFunction polynomials[] = new PolynomialFunction[n];
    for (int i = 0; i < n; i++) {
      polynomials[i] = new PolynomialFunction(yValues[i], b[i], c[i], d[i]);
    }

    return new PolynomialSplineFunction(xValues, polynomials);
  }
}
