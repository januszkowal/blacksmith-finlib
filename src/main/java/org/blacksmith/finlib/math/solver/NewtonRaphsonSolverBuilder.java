package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

/**
 * Builder for {@link NewtonRaphsonSolver} instances.
 */
public class NewtonRaphsonSolverBuilder extends AbstractSolverBuilder<SolverFunctionDerivative, Solver<SolverFunctionDerivative>> {

  public static NewtonRaphsonSolverBuilder builder() {
    return new NewtonRaphsonSolverBuilder();
  }

  @Override
  public NewtonRaphsonSolver build() {
    return new NewtonRaphsonSolver(this.iterations, this.tolerance, this.breakIfCandidateNotChanging);
  }
}
