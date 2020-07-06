package org.blacksmith.finlib.math.solver;

import java.util.function.Function;

public interface SolverBuilder<F extends SolverFunction,S extends Solver<F>> {
  SolverBuilder<F,S> tolerance(double tolerance);
  SolverBuilder<F,S> iterations(long iterations);
  SolverBuilder<F,S> argAligner(Function<Double,Double> argAligner);
  SolverBuilder<F,S> breakIfTheSameCandidate(boolean breakTheSameCandidate);
  S build();
}
