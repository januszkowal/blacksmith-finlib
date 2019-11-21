package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.xirr.Function;

public interface SolverBuilder {
  SolverBuilder withFunction(Function function);
  SolverBuilder withTolerance(double tolerance);
  SolverBuilder withIterations(long iterations);
  Solver build();
}
