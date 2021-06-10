package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public interface SolverBuilder<F extends UnivariateFunction> {
  SolverBuilder<F> tolerance(double tolerance);

  SolverBuilder<F> maxIterations(int iterations);

  SolverBuilder<F> minArg(double minArg);

  SolverBuilder<F> maxArg(double maxArg);

  SolverBuilder<F> breakIfCandidateNotChanging(boolean breakTheSameCandidate);

  Solver<F> build();
}
