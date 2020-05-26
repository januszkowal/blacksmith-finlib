package org.blacksmith.finlib.math.solver;

/**
 * Builder for {@link BisectionSolver} instances.
 */
public class BiSectionSolverBuilder extends AbstractSolverBuilder<Function,BisectionSolver> {

  private double minArg = Double.MIN_VALUE;
  private double maxArg = Double.MAX_VALUE;

  public BiSectionSolverBuilder() {
  }

  public static BiSectionSolverBuilder builder() {
    return new BiSectionSolverBuilder();
  }

  public BiSectionSolverBuilder withMinArg(double minArg) {
    this.minArg = minArg;
    return this;
  }

  public BiSectionSolverBuilder withMaxArg(double maxArg) {
    this.maxArg = maxArg;
    return this;
  }

  @Override
  public BisectionSolver build() {
    return new BisectionSolver(this.iterations, this.tolerance,
        this.minArg, this.maxArg);
  }
}
