package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

/**
 * Builder for {@link BiSectionSolver} instances.
 */
public class BiSectionSolverBuilder extends AbstractSolverBuilder<UnivariateFunction> {

  public static BiSectionSolverBuilder builder() {
    return new BiSectionSolverBuilder();
  }

  @Override
  public BiSectionSolver build() {
    return new BiSectionSolver(this.maxIterations, this.tolerance, this.minArg, this.maxArg, this.breakIfCandidateNotChanging);
  }
}
