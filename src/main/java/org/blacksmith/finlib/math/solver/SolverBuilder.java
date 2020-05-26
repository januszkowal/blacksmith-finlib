package org.blacksmith.finlib.math.solver;

public interface SolverBuilder<F extends Function,S extends Solver<F>> {
//  SolverBuilder<F,S> withFunction(F function);
  SolverBuilder<F,S> withTolerance(double tolerance);
  SolverBuilder<F,S> withIterations(long iterations);
  S build();
}
