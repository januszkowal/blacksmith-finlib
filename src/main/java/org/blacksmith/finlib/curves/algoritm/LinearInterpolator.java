package org.blacksmith.finlib.curves.algoritm;

public class LinearInterpolator implements PolynomialInterpolator {
  private final int MIN_SIZE = 2;
  public LinearInterpolator() {
  }

  @Override
  public PolynomialSplineFunction interpolate(double[] xvals, double[] yvals) {
    AlgorithmUtils.checkArraysSize(xvals, yvals);
    AlgorithmUtils.checkOrder(xvals);
    AlgorithmUtils.checkMinSize(xvals, MIN_SIZE);
    int n = xvals.length;
    final double[] af = new double[n];
    for (int i = 0; i < n - 1; i++) {
      af[i] = (yvals[i + 1] - yvals[i]) / (xvals[i + 1] - xvals[i]);
    }
    af[n - 1] = af[n - 2];
    double[] coefficients = new double[2];
    PolynomialSplineFunction.Polynomial[] polynominals = new PolynomialSplineFunction.Polynomial[xvals.length];
    for (int i = 0; i < xvals.length; i++) {
      coefficients[0] = yvals[i];
      coefficients[1] = af[i];
      polynominals[i] = new PolynomialSplineFunction.Polynomial(coefficients);
    }
    return new PolynomialSplineFunction(xvals, polynominals);
  }
}
