package org.blacksmith.finlib.math.analysis.interpolation.aaa;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationUtils;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialFunction;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialSplineFunction;

public class BSpline implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;

  @Override
  public InterpolatedFunction interpolate(double[] xValues, double[] yValues) {
    InterpolationUtils.checkMinSize(xValues, MIN_SIZE);
    InterpolationUtils.checkArraysSize(yValues, xValues.length,
        String.format("Y-values array should have the same size as X-values array. Expected: %d, actual: %d", xValues.length,
            yValues.length));
    InterpolationUtils.checkIncreasing(xValues, "X-values must increase");
    int intervals = xValues.length - 3;
    double[] hcoeff = hcoeff(xValues);
    double[] acoeff = acoeff(xValues, yValues, hcoeff);
    PolynomialFunction[] polynomials = new PolynomialFunction[intervals];
    for (int i = 0; i < intervals; i++) {
      polynomials[i] = polynomial(xValues, hcoeff, acoeff, i);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(double[] xValues, double[] hcoeff,
      double[] acoeff, int index) {
    int i = index + 1;
    double h = hcoeff[i];
    double hl = hcoeff[i - 1];
    double hr = hcoeff[i + 1];
    double a = hcoeff[i];
    double al = acoeff[i - 1];
    double ar = acoeff[i + 1];
    double xl = xValues[i - 1];
    double xr = xValues[i];

    return new PolynomialFunction() {
      @Override
      public double value(double x) {
        double d = (ar * (x - xl) + a * (xr - x + hr)) / (h + hr);
        double e = (a * (x - xl + hl) + al * (xl - x + h)) / (hl + h);
        return (d * (x - xl) + e * (xr - x)) / h;
      }
    };
  }

  private double[] hcoeff(double[] xValues) {
    double[] h = new double[xValues.length];
    int n = xValues.length - 2;
    for (int i = 1; i <= n; i++) {
      h[i] = xValues[i + 1] - xValues[i];
    }
    h[0] = h[1];
    h[n + 1] = h[n];
    return h;
  }

  private double[] acoeff(double[] xValues, double[] yValues, double[] h) {
    double delta, gamma, p, q, r;
    double[] a = new double[xValues.length];
    int n = xValues.length - 2;
    /* Determine A0 */
    delta = -1.0;
    gamma = 2.0 * yValues[0];
    p = delta * gamma;
    q = 2.0;
    for (int i = 1; i <= n; i++) {
      r = h[i + 1] / h[i];
      delta = -r * delta;
      gamma = -r * gamma + (r + 1.0) * yValues[i];
      p = p + gamma * delta;
      q = q + delta * delta;
    }
    a[0] = -p / q;
    /* Determine other coefficients Ai */
    for (int i = 1; i <= n + 1; i++) {
      a[i] = ((h[i - 1] + h[i]) * yValues[i - 1] - h[i] * a[i - 1]) / h[i - 1];
    }
    return a;
  }
}
