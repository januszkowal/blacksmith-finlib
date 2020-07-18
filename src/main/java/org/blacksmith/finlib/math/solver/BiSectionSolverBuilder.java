package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

/**
 * Builder for {@link BiSectionSolver} instances.
 */
public class BiSectionSolverBuilder extends AbstractSolverBuilder<SolverFunctionDerivative,Solver<SolverFunctionDerivative>> {

  public static BiSectionSolverBuilder builder() {
    return new BiSectionSolverBuilder();
  }

  @Override
  public BiSectionSolver build() {
    return new BiSectionSolver(this.iterations, this.tolerance, this.breakIfCandidateNotChanging);
  }
}
