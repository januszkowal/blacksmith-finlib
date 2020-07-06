package org.blacksmith.finlib.math.solver;

/**
 * Builder for {@link BisectionSolver} instances.
 */
public class BiSectionSolverBuilder extends AbstractSolverBuilder<SolverFunction,BisectionSolver> {

  private double minArg = Double.MIN_VALUE;
  private double maxArg = Double.MAX_VALUE;

  public BiSectionSolverBuilder() {
  }

  public static BiSectionSolverBuilder builder() {
    return new BiSectionSolverBuilder();
  }

  public BiSectionSolverBuilder minArg(double minArg) {
    this.minArg = minArg;
    return this;
  }

  public BiSectionSolverBuilder maxArg(double maxArg) {
    this.maxArg = maxArg;
    return this;
  }

  @Override
  public BisectionSolver build() {
    return new BisectionSolver(this.argAligner, this.iterations, this.tolerance,
        this.minArg, this.maxArg, this.breakIfTheSameCandidate);
  }
}
