package org.blacksmith.finlib.math.analysis.interpolation;

public abstract class AbstractSplineFunction {
  protected final double[] xValues;
  protected final PolynomialFunction[] polynomials;

  protected AbstractSplineFunction(double[] xValues, PolynomialFunction[] polynomials) {
    this.xValues = new double[xValues.length];
    this.polynomials = new PolynomialFunction[polynomials.length];
    System.arraycopy(xValues, 0, this.xValues, 0, xValues.length);
    System.arraycopy(polynomials, 0, this.polynomials, 0, polynomials.length);
  }

  public double[] getXValues() {
    double[] out = new double[xValues.length];
    System.arraycopy(xValues, 0, out, 0, xValues.length);
    return out;
  }

  public int getPolynomialCount() {
    return polynomials.length;
  }

  public double[] getPolynomialCoefficients(int polynomial) {
    return polynomials[polynomial].getCoefficients();
  }
}
