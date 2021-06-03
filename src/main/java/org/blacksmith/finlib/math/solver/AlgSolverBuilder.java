package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunction;

public class AlgSolverBuilder extends AbstractSolverBuilder<SolverFunction, Solver<SolverFunction>>
{

  private SolverAlgorithm algorithm = SolverAlgorithm.BI_SECTION;

  public AlgSolverBuilder() {
  }

  public static AlgSolverBuilder builder() {
    return new AlgSolverBuilder();
  }

  public static AlgSolverBuilder builder(SolverAlgorithm algorithm) {
    return new AlgSolverBuilder().algorithm(algorithm);
  }

  public AlgSolverBuilder algorithm(SolverAlgorithm algorithm) {
    this.algorithm = algorithm;
    return this;
  }

  @Override
  public Solver<SolverFunction> build() {
    if (this.algorithm == SolverAlgorithm.BI_SECTION) {
      return new BiSectionSolver(this.maxIterations, this.tolerance, this.breakIfCandidateNotChanging, minArg, maxArg);
    } else {
      return castSolver(new NewtonRaphsonSolver(this.maxIterations, this.tolerance, this.breakIfCandidateNotChanging), SolverFunction.class);
    }
  }

  public static <T extends SolverFunction> Solver<T> castSolver(Solver<?> solver, Class<T> cls) {
    return (Solver<T>)solver;
  }
}
