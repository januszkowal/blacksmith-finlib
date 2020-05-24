package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.Function;

public interface Function1stDerivative extends Function {
  double derivative(double arg);
}
