package org.blacksmith.finlib.math.solver;

/**
 * Builder for {@link NewtonRaphsonSolver} instances.
 */
public class NewtonRaphsonSolverBuilder extends AbstractSolverBuilder<SolverFunction1stDerivative, NewtonRaphsonSolver> {

  public NewtonRaphsonSolverBuilder() {
  }

  public static NewtonRaphsonSolverBuilder builder() {
    return new NewtonRaphsonSolverBuilder();
  }

  @Override
  public NewtonRaphsonSolver build() {
    return new NewtonRaphsonSolver(this.argAligner, this.iterations, this.tolerance, this.breakIfTheSameCandidate);
  }
}
