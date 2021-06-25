package org.blacksmith.finlib.math.analysis.interpolation;

/*
* The same as QuadraticSplineInterpolator
* */
public class NaturalSplineInterpolator extends AbstractPolynomialInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    validateKnots(xValues, yValues, MIN_SIZE);
    int n = xValues.length - 1;
    double[] h = new double[xValues.length];
    double[] b = new double[xValues.length];
    double[] u = new double[xValues.length];
    double[] v = new double[xValues.length];
    double[] z = new double[xValues.length];
    for (int i = 0; i < n; i++) {
      h[i] = xValues[i + 1] - xValues[i];
      b[i] = (yValues[i + 1] - yValues[i]) / h[i];
    }
    u[1] = 2.0 * (h[0] + h[1]);
    v[1] = 6.0 * (b[1] - b[0]);
    for (int i = 2; i < n; i++) {
      u[i] = 2.0 * (h[i] + h[i - 1]) - h[i - 1] * h[i - 1] / u[i - 1];
      v[i] = 6.0 * (b[i] - b[i - 1]) - h[i - 1] * v[i - 1] / u[i - 1];
    }
    z[0] = 0;
    z[n] = 0;
    for (int i = n - 1; i > 0; i--) {
      z[i] = (v[i] - h[i] * z[i + 1]) / u[i];
    }

    PolynomialFunction[] polynomials = new PolynomialFunction[n];
    for (int i = 0; i < n; i++) {
      polynomials[i] = polynomial(xValues, yValues, h, z, i);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(double[] xValues, double[] yValues, double[] h, double[] z, int index) {
    double a = yValues[index];
    double b = -(h[index] / 6.0d) * (z[index + 1] + 2.0d * z[index]) + (yValues[index + 1] - yValues[index]) / h[index];
    double c = 0.5d * z[index];
    double d = (z[index + 1] - z[index]) / (6.0d * h[index]);
    return new PolynomialFunction(a, b, c, d);
  }
}
