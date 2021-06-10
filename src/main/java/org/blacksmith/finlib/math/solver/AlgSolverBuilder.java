package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public class AlgSolverBuilder extends AbstractSolverBuilder<UnivariateFunction>
{

  private SolverAlgorithm algorithm = SolverAlgorithm.BI_SECTION;

  private AlgSolverBuilder() {
  }

  public static AlgSolverBuilder builder() {
    return new AlgSolverBuilder();
  }


  public AlgSolverBuilder algorithm(SolverAlgorithm algorithm) {
    this.algorithm = algorithm;
    return this;
  }

  @Override
  public Solver<UnivariateFunction> build() {
    if (this.algorithm == SolverAlgorithm.BI_SECTION) {
      return new BiSectionSolver(this.maxIterations, this.tolerance, minArg, maxArg, this.breakIfCandidateNotChanging);
    } else {
      return castSolver(new NewtonRaphsonSolver(this.maxIterations, this.tolerance, this.breakIfCandidateNotChanging), UnivariateFunction.class);
    }
  }

  public static <T extends UnivariateFunction> Solver<T> castSolver(Solver<?> solver, Class<T> cls) {
    return (Solver<T>)solver;
  }
}
