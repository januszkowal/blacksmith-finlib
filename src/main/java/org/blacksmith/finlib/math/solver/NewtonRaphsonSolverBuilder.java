package org.blacksmith.finlib.math.solver;

/**
 * Builder for {@link NewtonRaphsonSolver} instances.
 */
public class NewtonRaphsonSolverBuilder extends AbstractSolverBuilder<NewtonRaphsonSolver, Function1stDeriv> {

  public NewtonRaphsonSolverBuilder() {
  }

  public static NewtonRaphsonSolverBuilder builder() {
    return new NewtonRaphsonSolverBuilder();
  }

  @Override
  public NewtonRaphsonSolver build() {
    return new NewtonRaphsonSolver(this.function, this.iterations, this.tolerance);
  }
}
