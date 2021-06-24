package org.blacksmith.finlib.math.analysis.interpolation;

public class LinearInterpolator extends AbstractPolynomialInterpolator implements PolynomialInterpolator {
  private static final int MIN_SIZE = 2;
  public LinearInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xValues, double[] yValues) {
    validateKnots(xValues, yValues, MIN_SIZE);
    int intervals = xValues.length;
    final double[] af = new double[intervals];
    for (int i = 0; i < intervals - 1; i++) {
      af[i] = (yValues[i + 1] - yValues[i]) / (xValues[i + 1] - xValues[i]);
    }
    af[intervals - 1] = af[intervals - 2];
    PolynomialFunction[] polynomials = new PolynomialFunction[intervals];
    for (int i = 0; i < intervals; i++) {
      polynomials[i] = polynomial(xValues, yValues, af, i);
    }
    return new PolynomialSplineFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(double[] xValues, double[] yValues, double[] af, int index) {
    double a = yValues[index];
    double b = af[index];
    return new PolynomialFunction(a, b);
  }
}
