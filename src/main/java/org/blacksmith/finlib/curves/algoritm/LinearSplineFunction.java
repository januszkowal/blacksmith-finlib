package org.blacksmith.finlib.curves.algoritm;

public class LinearSplineFunction implements PolynominalFunction {
  private final double[] xvals;
  private final double[] yvals;
  private LinearPolynominal[] polynominals;

  public LinearSplineFunction(double[] xvals, double[] yvals, LinearPolynominal[] polynominals) {
    this.xvals = xvals;
    this.yvals = yvals;
    this.polynominals = polynominals;
  }

  public double value(double x) {
    int index = AlgorithmUtils.binarySearchA(this.xvals, x);
    return valueY1(index, x);
  }

  protected double valueY1(int index, double x) {
    return polynominals[index].value(x);
  }

  public static class LinearPolynominal {

    final double coefficient0;
    final double coefficient1;

    public LinearPolynominal(double coefficient0, double coefficient1) {
      this.coefficient0 = coefficient0;
      this.coefficient1 = coefficient1;
    }

    public double value(double x) {
      return coefficient0 + coefficient1 * x;
    }
  }
}
