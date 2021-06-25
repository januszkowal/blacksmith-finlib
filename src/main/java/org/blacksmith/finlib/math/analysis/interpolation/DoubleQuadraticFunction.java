package org.blacksmith.finlib.math.analysis.interpolation;

public class DoubleQuadraticFunction extends AbstractSplineFunction implements InterpolatedFunction {
  private static long NEGATIVE_ZERO_BITS = Double.doubleToRawLongBits(-0d);
  private final int lastInterval;
  private final int intervalCount;

  public DoubleQuadraticFunction(double[] xValues, PolynomialFunction[] polynomials) {
    super(xValues, polynomials);
    this.intervalCount = polynomials.length;
    this.lastInterval = polynomials.length - 1;
  }

  @Override
  public double value(double x) {
    int index = getKnotIndex0(x);
    // at start of curve - singe quadratic
    if (index == 0) {
      PolynomialFunction polynomial = polynomials[0];
      return polynomial.value(x - xValues[1]);
    }
    // at end of curve - single quadratic
    else if (index == intervalCount) {
      PolynomialFunction polynomial = polynomials[lastInterval];
      return polynomial.value(x - xValues[lastInterval + 1]);
    }
    double w = (xValues[index + 1] - x) / (xValues[index + 1] - xValues[index]);
    PolynomialFunction polynomial1 = polynomials[index - 1];
    PolynomialFunction polynomial2 = polynomials[index];
    return w * polynomial1.value(x - xValues[index]) + (1 - w) *
        polynomial2.value(x - xValues[index + 1]);
  }

  public int getKnotIndex0(double key) {
    int index = InterpolationUtils.getKnotIndex0(this.xValues, key);
    if (index < 0) {
      index = 0;
    } else if (index > intervalCount) {
      index = intervalCount;
    }
    return index;
  }
}
