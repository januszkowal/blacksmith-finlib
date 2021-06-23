package org.blacksmith.finlib.math.analysis.interpolation;

public class PolynomialSplineFunction extends AbstractPolynomialSplineFunction implements InterpolatedFunction {

  public PolynomialSplineFunction(double[] xValues, PolynomialFunction[] polynomials) {
    super(xValues, polynomials);
  }

  @Override
  public double value(double x) {
    int index = getKnotIndex0(x);
    return polynomials[index].value(x - xValues[index]);
  }
}
