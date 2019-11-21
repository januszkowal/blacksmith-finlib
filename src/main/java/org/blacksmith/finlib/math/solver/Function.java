package org.blacksmith.finlib.math.solver;

public interface Function {
  double functionValue(double arg);
  double derivativeValue(double arg);
}
