package org.blacksmith.finlib.curves.algoritm;

public class LinearSplineFunction {
  private final double[] xvals;
  private final double[] yvals;
  private LinearPolynominal[] polynominals;

  public LinearSplineFunction(double[] xvals, double[] yvals, LinearPolynominal[] polynominals) {
    int n = xvals.length;
    this.xvals = new double[n];
    this.yvals = new double[n];
    System.arraycopy(xvals, 0, this.xvals, 0, n);
    System.arraycopy(yvals, 0, this.yvals, 0, n);
    this.polynominals = polynominals;
  }

  public double value(double v) {
    int index = AlgorithmUtils.getKnotIndex(this.xvals, v);
    return valueY1(index, v - xvals[index]);
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

    public double value(double v) {
      return coefficient1 * v + coefficient0;
    }
  }
}
