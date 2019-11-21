package org.blacksmith.finlib.math.solver;

public interface Function {
  double presentValue(double arg);
  double derivative(double arg);
}
