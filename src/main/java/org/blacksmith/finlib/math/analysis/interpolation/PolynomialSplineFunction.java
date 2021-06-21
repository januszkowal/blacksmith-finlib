package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

import lombok.Value;

public class PolynomialSplineFunction implements InterpolatedFunction {
  protected final double[] xValues;
  protected final PolynomialFunction[] polynomials;
  protected final int lastInterval;

  public PolynomialSplineFunction(double[] xValues, PolynomialFunction[] polynomials) {
    this.xValues = new double[xValues.length];
    this.polynomials = new PolynomialFunction[polynomials.length];
    System.arraycopy(xValues, 0, this.xValues, 0, xValues.length);
    System.arraycopy(polynomials, 0, this.polynomials, 0, polynomials.length);
    this.lastInterval = polynomials.length - 1;
  }

  @Override
  public double value(double x) {
    int index = getKnotIndex0(x);
    return polynomials[index].value(x - xValues[index]);
  }

  @Override
  public double[] getXValues() {
    double[] out = new double[xValues.length];
    System.arraycopy(xValues, 0, out, 0, xValues.length);
    return out;
  }

  public int getKnotIndex1(double key) {
    int index = Arrays.binarySearch(this.xValues, key);
    if (index < 0) {
      index = -index - 2;
    }
    if (index > lastInterval) {
      index = lastInterval;
    }
    return index;
  }

  public int getKnotIndex0(double key) {
    int index = InterpolationUtils.getKnotIndex0(this.xValues, key);
    if (index > lastInterval) {
      index = lastInterval;
    }
    return index;
  }

  public UnivariateFunction derivative() {
    return polynomialSplineDerivative();
  }

  public PolynomialSplineFunction polynomialSplineDerivative() {
    PolynomialFunction[] derivativePolynomials = new PolynomialFunction[polynomials.length];
    for (int i = 0; i < polynomials.length; i++) {
      derivativePolynomials[i] = polynomials[i].polynomialDerivative();
    }
    return new PolynomialSplineFunction(xValues, derivativePolynomials);
  }

  public List<PolynomialFunctionMetadata> metadata() {
    return IntStream.range(0, polynomials.length)
        .mapToObj(i -> PolynomialFunctionMetadata.of(xValues[i], IntStream.range(0, polynomials[i].coefficients.length)
            .mapToObj(coeff -> polynomials[i].coefficients[coeff]).collect(Collectors.toList())))
        .collect(Collectors.toList());
  }
}
