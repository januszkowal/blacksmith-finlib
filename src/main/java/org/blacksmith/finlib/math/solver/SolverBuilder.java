package org.blacksmith.finlib.math.solver;

public interface SolverBuilder<S extends Solver,F extends Function> {
  SolverBuilder<S,F> withFunction(F function);
  SolverBuilder<S,F> withTolerance(double tolerance);
  SolverBuilder<S,F> withIterations(long iterations);
  S build();
}
