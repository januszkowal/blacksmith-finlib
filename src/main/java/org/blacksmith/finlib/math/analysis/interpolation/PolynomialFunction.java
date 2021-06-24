package org.blacksmith.finlib.math.analysis.interpolation;

import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public class PolynomialFunction implements UnivariateFunction {
  protected final double [] coefficients;
  int n;

  public PolynomialFunction(double... coefficients) {
    n = coefficients.length;
    if (n == 0) {
      throw new IllegalArgumentException("Empty coefficients array");
    }
    while ((n > 1) && (coefficients[n - 1] == 0)) {
      --n;
    }
    this.coefficients = new double[n];
    System.arraycopy(coefficients, 0, this.coefficients, 0, n);
  }

  protected static double[] differentiate(double[] coefficients)
      throws NullArgumentException, NoDataException {
    ArgChecker.notNull(coefficients);
    int n = coefficients.length;
    if (n == 0) {
      throw new IllegalArgumentException("Empty polynomials coefficients array");
    }
    if (n == 1) {
      return new double[]{ 0 };
    }
    double[] result = new double[n - 1];
    for (int i = n - 1; i > 0; i--) {
      result[i - 1] = i * coefficients[i];
    }
    return result;
  }

  private double evaluate(double x) {
    double y = coefficients[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      y = x * y + coefficients[i];
    }
    return y;
  }

  @Override
  public double value(double x) {
    return evaluate(x);
  }

  public PolynomialFunction polynomialDerivative() {
    return new PolynomialFunction(differentiate(coefficients));
  }

  public UnivariateFunction derivative() {
    return polynomialDerivative();
  }

  public double[] getCoefficients() {
    double[] out = new double[coefficients.length];
    System.arraycopy(coefficients, 0, out, 0, coefficients.length);
    return out;
  }
}
