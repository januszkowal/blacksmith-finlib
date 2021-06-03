package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunction;

/**
 * Builder for {@link BiSectionSolver} instances.
 */
public class BiSectionSolverBuilder extends AbstractSolverBuilder<SolverFunction, Solver<SolverFunction>> {

  public static BiSectionSolverBuilder builder() {
    return new BiSectionSolverBuilder();
  }

  @Override
  public BiSectionSolver build() {
    return new BiSectionSolver(this.maxIterations, this.tolerance, this.breakIfCandidateNotChanging, this.minArg, this.maxArg);
  }
}
