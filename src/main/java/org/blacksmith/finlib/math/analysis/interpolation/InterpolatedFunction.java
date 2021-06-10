package org.blacksmith.finlib.math.analysis.interpolation;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public interface InterpolatedFunction extends UnivariateFunction {
  double[] getKnots();
}
