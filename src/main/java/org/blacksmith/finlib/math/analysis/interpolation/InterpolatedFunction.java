package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.Arrays;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public interface InterpolatedFunction extends UnivariateFunction {
  double[] getXValues();
  default double[] values(double... x) {
    return Arrays.stream(x).map(this::value).toArray();
  }
}
