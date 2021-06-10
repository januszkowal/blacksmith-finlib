package org.blacksmith.finlib.math.analysis.interpolation;

import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public class PolynomialFunction implements UnivariateFunction {
  double[] coefficients;

  public PolynomialFunction(double[] coefficients) {
    int n = coefficients.length;
    if (n == 0) {
      throw new IllegalArgumentException("Empty coefficients array");
    }
    while ((n > 1) && (coefficients[n - 1] == 0)) {
      --n;
    }
    this.coefficients = new double[n];
    System.arraycopy(coefficients, 0, this.coefficients, 0, n);
  }

  public double evaluate(double x) {
    int n = coefficients.length;
    double result = coefficients[n - 1];
    for (int i = n - 2; i >= 0; i--) {
      result = x * result + coefficients[i];
    }
    return result;
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

  protected static double[] differentiate(double[] coefficients)
      throws NullArgumentException, NoDataException {
    ArgChecker.notNull(coefficients);
    int n = coefficients.length;
    if (n == 0) {
      throw new IllegalArgumentException("Empty polynomials coefficients array");
    }
    if (n == 1) {
      return new double[]{0};
    }
    double[] result = new double[n - 1];
    for (int i = n - 1; i > 0; i--) {
      result[i - 1] = i * coefficients[i];
    }
    return result;
  }
}
