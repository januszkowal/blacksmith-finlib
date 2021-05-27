package org.blacksmith.finlib.curves.algoritm;

import java.util.Arrays;

public class PolynomialSplineFunction implements SingleArgumentFunction {
  private final double[] knots;
  private final Polynominal[] polynominals;

  public PolynomialSplineFunction(double[] knots, Polynominal[] polynominals) {
    int n = knots.length;
    this.knots = new double[n];
    this.polynominals = new Polynominal[n];
    System.arraycopy(knots, 0, this.knots, 0, n);
    System.arraycopy(polynominals, 0, this.polynominals, 0, n);
  }

  public double value(double v) {
    int index = AlgorithmUtils.getKnotIndex(this.knots, v);
    return valueY1(index, v - knots[index]);
  }

  protected double valueY1(int index, double x) {
    return polynominals[index].value(x);
  }
  
  public static class Polynominal {
    double[] coefficients;

    public Polynominal(double[] coefficients) {
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

    public double value(double x) {
      return evaluate(coefficients, x);
    }

    private double evaluate(double[] coefficients, double x) {
      int n = coefficients.length;
      double result = coefficients[n - 1];
      for (int j = n - 2; j >= 0; j--) {
        result = x * result + coefficients[j];
      }
      return result;
    }
  }
}
