package org.blacksmith.finlib.curves.algoritm;

public class LinearInterpolator {
  public LinearInterpolator() {
  }

  public LinearSplineFunction interpolate(double[] xvals, double[] yvals) {
    AlgorithmUtils.checkOrder(xvals);
    final double[] af = new double[xvals.length];
    for (int i = 0; i < xvals.length - 1; i++) {
      af[i] = (yvals[i + 1] - yvals[i]) / (xvals[i + 1] - xvals[i]);
    }
    af[xvals.length - 1] = af[xvals.length - 2];
    LinearSplineFunction.LinearPolynominal[] polynominals = new LinearSplineFunction.LinearPolynominal[xvals.length];
    for (int i = 0; i < xvals.length; i++) {
      polynominals[i] = new LinearSplineFunction.LinearPolynominal(yvals[i], af[i]);
    }
    return new LinearSplineFunction(xvals, yvals, polynominals);
  }
}
