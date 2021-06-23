package org.blacksmith.finlib.math.analysis.interpolation.aaa;

import org.blacksmith.finlib.math.analysis.interpolation.PolynomialFunction;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialSplineFunction;

public class PolynomialSplineFunction2 extends PolynomialSplineFunction {
  public PolynomialSplineFunction2(double[] knots, PolynomialFunction[] polynomials) {
    super(knots, polynomials);
  }

  @Override
  public double value(double x) {
    int lowerIndex = getKnotIndex0(x);
    int higherIndex = lowerIndex + 1;
    // at start of curve
    if (lowerIndex <= 0) {
      PolynomialFunction polynomial = polynomials[0];
      double xx = x - xValues[1];
      return polynomial.value(x - xValues[1]);
    }
    // at end of curve
    if (higherIndex >= lastInterval) {
      PolynomialFunction polynomial = polynomials[lastInterval];
      return polynomial.value(x - xValues[lastInterval]);
    }
    // normal case
    double w = (xValues[higherIndex] - x) / (xValues[higherIndex] - xValues[lowerIndex]);
    PolynomialFunction polynomial1 = polynomials[lowerIndex];
    PolynomialFunction polynomial2 = polynomials[higherIndex];
    //    return polynomial1.value(x - xValues[lowerIndex]);
    return w * polynomial1.value(x - xValues[lowerIndex]) + (1 - w) *
        polynomial2.value(x - xValues[higherIndex]);
  }
}
