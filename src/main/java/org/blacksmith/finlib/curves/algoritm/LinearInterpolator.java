package org.blacksmith.finlib.curves.algoritm;

public class LinearInterpolator {
  private final int MIN_SIZE = 2;
  public LinearInterpolator() {
  }

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
    PolynomialSplineFunction.Polynominal[] polynominals = new PolynomialSplineFunction.Polynominal[xvals.length];
    for (int i = 0; i < xvals.length; i++) {
      coefficients[0] = yvals[i];
      coefficients[1] = af[i];
      polynominals[i] = new PolynomialSplineFunction.Polynominal(coefficients);
    }
    return new PolynomialSplineFunction(xvals, polynominals);
  }
}
