package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

public class AlgSolverBuilder extends AbstractSolverBuilder<SolverFunctionDerivative, Solver<SolverFunctionDerivative>>
    implements SolverBuilder<SolverFunctionDerivative,Solver<SolverFunctionDerivative>> {

  private SolverAlgorithm algorithm = SolverAlgorithm.BI_SECTION;

  public enum SolverAlgorithm {
    BI_SECTION,
    NEWTON_RAPHSON
  }

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
  public Solver<SolverFunctionDerivative> build() {
    if (this.algorithm == SolverAlgorithm.BI_SECTION) {
      return new BiSectionSolver(this.iterations, this.tolerance, this.breakIfCandidateNotChanging);
    } else {
      return new NewtonRaphsonSolver(this.iterations, this.tolerance, this.breakIfCandidateNotChanging);
    }
  }
}
