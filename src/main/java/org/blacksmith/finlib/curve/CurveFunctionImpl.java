package org.blacksmith.finlib.curve;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curve.algorithm.PolynomialFunction;

public class CurveFunctionImpl implements CurveFunction {
  private final Set<Integer> knotSet;
  private final PolynomialFunction function;

  public CurveFunctionImpl(PolynomialFunction function) {
    this.function = function;
    this.knotSet = Arrays.stream(getKnots())
        .mapToInt(x -> (int)Math.ceil(x))
        .boxed()
        .collect(Collectors.toSet());
  }
  public double value(int x) {
    return function.value(x);
  }

  @Override
  public double[] getKnots() {
    return function.getKnots();
  }

  @Override
  public double value(double x) {
    return function.value(x);
  }

  @Override
  public boolean isKnot(int x) {
    return this.knotSet.contains(x);
  }
}