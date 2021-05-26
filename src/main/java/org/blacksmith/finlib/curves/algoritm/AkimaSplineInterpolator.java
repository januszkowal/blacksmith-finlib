package org.blacksmith.finlib.curves.algoritm;

public class AkimaSplineInterpolator {

  public AkimaSplineInterpolator() {}

  public AkimaSplineFunction interpolate(double[] xvals, double[] yvals) {
    AlgorithmUtils.checkOrder(xvals);
    int n = xvals.length;
    AkimaSplineFunction.AkimaPolynominal[] polynominals = new AkimaSplineFunction.AkimaPolynominal[n];
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
    for (int i = 0; i < xvals.length - 1; i++) {
      polynominals[i] = new AkimaSplineFunction.AkimaPolynominal(xvals[i], yvals[i], xvals[i + 1] - xvals[i], firstDerivatives[i], firstDerivatives[i + 1], secants[i + 2]);
    }
    return new AkimaSplineFunction(xvals, yvals, polynominals);
  }
}
