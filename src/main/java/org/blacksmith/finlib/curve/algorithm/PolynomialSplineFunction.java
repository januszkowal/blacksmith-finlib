package org.blacksmith.finlib.curve.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.blacksmith.finlib.curve.types.CurvePoint;

public class PolynomialSplineFunction implements PolynomialFunction {
  private final double[] knots;
  private final Polynomial[] polynomials;
  private final int lastKPIndex;

  public PolynomialSplineFunction(double[] knots, Polynomial[] polynomials) {
    this.knots = new double[knots.length];
    this.polynomials = new Polynomial[polynomials.length];
    System.arraycopy(knots, 0, this.knots, 0, knots.length);
    System.arraycopy(polynomials, 0, this.polynomials, 0, polynomials.length);
    this.lastKPIndex = Math.min(polynomials.length - 1, knots.length - 1);
  }

  @Override
  public double value(double x) {
    int index = getKnotIndex(x);
    return polynomialValue(index, x);
  }

  @Override
  public double[] getKnots() {
    double out[] = new double[knots.length];
    System.arraycopy(knots, 0, out, 0, knots.length);
    return out;
  }

  //@Override
  public List<CurvePoint> valuesForRange(int min, int max) {
    List<CurvePoint> points = new ArrayList<>(max - min + 1);
    var ranges = AlgorithmUtils.getCalculationRanges(min, max, getKnots(), polynomials.length);
    for (AlgorithmUtils.CalcRange range : ranges) {
      points.add(CurvePoint.of(range.start, polynomialValue(range.knotIndex, range.start), true));
      for (int j = range.start + 1; j <= range.end; j++) {
        points.add(CurvePoint.of(j, polynomialValue(range.knotIndex, j), false));
      }
    }
    return points;
  }

  public int getKnotIndex1(double key) {
    int index = Arrays.binarySearch(this.knots, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index > lastKPIndex) {
      index = lastKPIndex;
    }
    return index;
  }

  public int getKnotIndex(double key) {
    int index = AlgorithmUtils.getKnotIndex0(this.knots, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index > lastKPIndex) {
      index = lastKPIndex;
    }
    return index;
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
      for (int i = n - 2; i >= 0; i--) {
        result = x * result + coefficients[i];
      }
      return result;
    }
  }

  protected double polynomialValue(int index, double x) {
    return polynomials[index].value(x - knots[index]);
  }
}
