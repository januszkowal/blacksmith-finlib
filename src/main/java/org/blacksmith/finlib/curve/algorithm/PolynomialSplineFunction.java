package org.blacksmith.finlib.curve.algorithm;

import java.util.Arrays;

public class PolynomialSplineFunction implements InterpolatedFunction {
  private final double[] knots;
  private final PolynomialFunction[] polynomials;
  private final int lastKPIndex;

  public PolynomialSplineFunction(double[] knots, PolynomialFunction[] polynomials) {
    this.knots = new double[knots.length];
    this.polynomials = new PolynomialFunction[polynomials.length];
    System.arraycopy(knots, 0, this.knots, 0, knots.length);
    System.arraycopy(polynomials, 0, this.polynomials, 0, polynomials.length);
    this.lastKPIndex = Math.min(polynomials.length - 1, knots.length - 1);
  }

  @Override
  public double value(double x) {
    int index = getKnotIndex0(x);
    return polynomialValue(index, x);
  }

  @Override
  public double[] getKnots() {
    double[] out = new double[knots.length];
    System.arraycopy(knots, 0, out, 0, knots.length);
    return out;
  }

  public int getKnotIndex1(double key) {
    int index = Arrays.binarySearch(this.knots, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index > lastKPIndex) {
      index = lastKPIndex;
    }
    return index;
  }

  public int getKnotIndex0(double key) {
    int index = AlgorithmUtils.getKnotIndex0(this.knots, key);
    if (index > lastKPIndex) {
      index = lastKPIndex;
    }
    return index;
  }

  public UnivariateFunction derivative() {
    return polynomialSplineDerivative();
  }

  public PolynomialSplineFunction polynomialSplineDerivative() {
    PolynomialFunction[] derivativePolynomials = new PolynomialFunction[polynomials.length];
    for (int i = 0; i < polynomials.length; i++) {
      derivativePolynomials[i] = polynomials[i].polynomialDerivative();
    }
    return new PolynomialSplineFunction(knots, derivativePolynomials);
  }

  protected double polynomialValue(int index, double x) {
    return polynomials[index].evaluate(x - knots[index]);
  }
}
