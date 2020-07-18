package org.blacksmith.finlib.math.solver.function;

public interface SolverFunction {
  default double alignCandidate(double candidate) {
    return candidate;
  }
  double getValue(double arg);
}
