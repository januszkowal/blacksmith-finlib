package org.blacksmith.finlib.curves.algoritm;

import java.util.ArrayList;
import java.util.List;

import org.blacksmith.finlib.curves.types.CurvePoint;

public class PolynomialSplineFunction implements SingleArgumentFunction {
  private final double[] knots;
  private final Polynomial[] polynomials;

  public PolynomialSplineFunction(double[] knots, Polynomial[] polynominals) {
    this.knots = new double[knots.length];
    this.polynomials = new Polynomial[polynominals.length];
    System.arraycopy(knots, 0, this.knots, 0, knots.length);
    System.arraycopy(polynominals, 0, this.polynomials, 0, polynominals.length);
  }

  @Override
  public double value(double v) {
    int index = AlgorithmUtils.getKnotIndex(this.knots, v);
    if (index >= polynomials.length ) {
      index = polynomials.length - 1;
    }
    return valueY1(index, v - knots[index]);
  }

  @Override
  public double[] getKnots() {
    double out[] = new double[knots.length];
    System.arraycopy(knots, 0, out, 0, knots.length);
    return out;
  }

  @Override
  public List<CurvePoint> values(int min, int max) {
    List<CurvePoint> points = new ArrayList<>(max - min + 1);
    var ranges = AlgorithmUtils.getCalculationRanges(min, max, getKnots(), polynomials.length);
    for (AlgorithmUtils.CalcRange range : ranges) {
      points.add(CurvePoint.of(range.start, valueY1(range.knotIndex, range.start - knots[range.knotIndex]), true));
      for (int j = range.start + 1; j <= range.end; j++) {
        points.add(CurvePoint.of(j, valueY1(range.knotIndex, j - knots[range.knotIndex]), false));
      }
    }
    return points;
  }

  public static class Polynomial {
    double[] coefficients;

    public Polynomial(double[] coefficients) {
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

  protected double valueY1(int index, double x) {
    return polynomials[index].value(x);
  }
}
