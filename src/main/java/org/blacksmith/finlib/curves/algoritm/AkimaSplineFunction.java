package org.blacksmith.finlib.curves.algoritm;

public class AkimaSplineFunction implements PolynominalFunction {
  private final double[] xvals;
  private final double[] yvals;
  private AkimaPolynominal[] polynominals;

  public AkimaSplineFunction(double[] xvals, double[] yvals, AkimaPolynominal[] polynominals) {
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
  
  public static class AkimaPolynominal {
    final double xb0;
    final double coefficient0;
    final double coefficient1;
    final double coefficient2;
    private final double coefficient3;

    public AkimaPolynominal(double xb0, double coefficient0, double b, double firstDerivative, double t1, double m2) {
      this.xb0 = xb0;
      this.coefficient0 = coefficient0;
      this.coefficient1 = firstDerivative;
      this.coefficient2 = (3.0d * m2 - 2.0d * firstDerivative - t1) / b;
      this.coefficient3 = (-2.0d * m2 + firstDerivative + t1) / (b * b);
    }

    public double value(double x) {
      double a = x - xb0;
      return coefficient0 + coefficient1 * a + coefficient2 * a * a + coefficient3 * a * a * a;
    }
  }
}
