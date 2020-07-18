package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunction;

public interface SolverBuilder<F extends SolverFunction,S extends Solver<F>> {
  SolverBuilder<F,S> tolerance(double tolerance);
  SolverBuilder<F,S> iterations(long iterations);
//  SolverBuilder<F,S> minArg(double minArg);
//  SolverBuilder<F,S> maxArg(double maxArg);
  SolverBuilder<F,S> breakIfCandidateNotChanging(boolean breakTheSameCandidate);
  SolverBuilder<F,S> asBuilder();
  Solver<F> build();
}
