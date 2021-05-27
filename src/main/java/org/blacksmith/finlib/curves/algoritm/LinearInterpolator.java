package org.blacksmith.finlib.curves.algoritm;

public class LinearInterpolator {
  public LinearInterpolator() {
  }

  public PolynomialSplineFunction interpolate(double[] xvals, double[] yvals) {
    AlgorithmUtils.checkOrder(xvals);
    int n = xvals.length;
    final double[] af = new double[n];
    for (int i = 0; i < n - 1; i++) {
      af[i] = (yvals[i + 1] - yvals[i]) / (xvals[i + 1] - xvals[i]);
    }
    af[n - 1] = af[n - 2];
    double[] coefficents = new double[2];
    PolynomialSplineFunction.Polynominal[] polynominals = new PolynomialSplineFunction.Polynominal[xvals.length];
    for (int i = 0; i < xvals.length; i++) {
      coefficents[0] = yvals[i];
      coefficents[1] = af[i];
      polynominals[i] = new PolynomialSplineFunction.Polynominal(coefficents);
    }
    return new PolynomialSplineFunction(xvals, polynominals);
  }
}
