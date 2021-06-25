package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public class PolynomialSplineFunction extends AbstractSplineFunction implements InterpolatedFunction {

  private static long NEGATIVE_ZERO_BITS = Double.doubleToRawLongBits(-0d);

  protected final int lastInterval;

  public PolynomialSplineFunction(double[] xValues, PolynomialFunction[] polynomials) {
    super(xValues, polynomials);
    this.lastInterval = polynomials.length - 1;
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
    if (index < 0) {
      index = 0;
    } else if (index > lastInterval) {
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

  @Override
  public double value(double x) {
    int index = getKnotIndex0(x);
    return polynomials[index].value(x - xValues[index]);
  }
}
