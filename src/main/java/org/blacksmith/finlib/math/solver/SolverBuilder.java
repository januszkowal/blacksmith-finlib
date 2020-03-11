package org.blacksmith.finlib.math.solver;

public interface SolverBuilder {
  SolverBuilder withFunction(Function function);
  SolverBuilder withTolerance(double tolerance);
  SolverBuilder withIterations(long iterations);
  Solver build();
}
