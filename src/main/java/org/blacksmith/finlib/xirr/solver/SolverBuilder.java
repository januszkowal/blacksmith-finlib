package org.blacksmith.finlib.xirr.solver;

import org.blacksmith.finlib.xirr.Function;

public interface SolverBuilder {
  SolverBuilder withFunction(Function function);
  SolverBuilder withTolerance(double tolerance);
  SolverBuilder withIterations(long iterations);
  Solver build();
}
