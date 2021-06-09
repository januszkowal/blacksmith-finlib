package org.blacksmith.finlib.curve.algorithm;

public interface InterpolatedFunction extends UnivariateFunction {
  double[] getKnots();
}
