package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;

/**
 * Builder for {@link NewtonRaphsonSolver} instances.
 */
public class NewtonRaphsonSolverBuilder extends AbstractSolverBuilder<UnivariateDifferentiableFunction> {

  public static NewtonRaphsonSolverBuilder builder() {
    return new NewtonRaphsonSolverBuilder();
  }

  @Override
  public NewtonRaphsonSolver build() {
    return new NewtonRaphsonSolver(this.maxIterations, this.tolerance, this.breakIfCandidateNotChanging);
  }
}
