package org.blacksmith.finlib.math.analysis.interpolation;

public class DoubleQuadraticInterpolator extends AbstractPolynomialInterpolator implements PolynomialInterpolator {

  private static final int MIN_SIZE = 1;

  public DoubleQuadraticFunction interpolate(double[] xValues, double[] yValues) {
    validateKnots(xValues, yValues, MIN_SIZE);
    return polynomials(xValues, yValues);
  }

  public PolynomialFunction firstDerivative(double[] x, double[] y, int index) {
    double dx1 = x[index] - x[index - 1];
    double dx2 = x[index + 1] - x[index];
    double dy1 = y[index] - y[index - 1];
    double dy2 = y[index + 1] - y[index];
    double b = (dx1 * dy2 / dx2 + dx2 * dy1 / dx1) / (dx1 + dx2);
    double c = 2. * (dy2 / dx2 - dy1 / dx1) / (dx1 + dx2);
    return new PolynomialFunction(new double[]{ b, c });
  }

  private DoubleQuadraticFunction polynomials(double[] xValues, double[] yValues) {
    if (xValues.length == 1) {
      double a = yValues[1];
      double b = (yValues[1] - yValues[0]) / (xValues[1] - xValues[0]);
      return new DoubleQuadraticFunction(xValues, new PolynomialFunction[]{ new PolynomialFunction(a, b) });
    }
    int polynomialsCount = xValues.length - 2;
    PolynomialFunction[] polynomials = new PolynomialFunction[polynomialsCount];
    for (int i = 0; i < polynomialsCount; i++) {
      polynomials[i] = polynomial(xValues, yValues, i);
    }
    return new DoubleQuadraticFunction(xValues, polynomials);
  }

  private PolynomialFunction polynomial(double[] xValues, double[] yValues, int index) {
    return new PolynomialFunction(coefficients(xValues, yValues, index));
  }

  private double[] coefficients(double[] xValues, double[] yValues, int index) {
    double dx1 = xValues[index + 1] - xValues[index];
    double dy1 = yValues[index + 1] - yValues[index];
    double dx2 = xValues[index + 2] - xValues[index + 1];
    double dy2 = yValues[index + 2] - yValues[index + 1];
    double a = yValues[index + 1];
    double b = (dx1 * dy2 / dx2 + dx2 * dy1 / dx1) / (dx1 + dx2);
    double c = (dy2 / dx2 - dy1 / dx1) / (dx1 + dx2);
    return new double[]{ a, b, c };
  }

  private PolynomialFunction[] quadraticsFirstDerivative(double[] x, double[] y) {
    int count = x.length - 1;
    if (count == 1) {
      double b = (y[1] - y[0]) / (x[1] - x[0]);
      return new PolynomialFunction[]{ new PolynomialFunction(b) };
    } else {
      PolynomialFunction[] quadraticFirstDerivative = new PolynomialFunction[count - 1];
      for (int i = 1; i < count; i++) {
        quadraticFirstDerivative[i - 1] = firstDerivative(x, y, i);
      }
      return quadraticFirstDerivative;
    }
  }
}
