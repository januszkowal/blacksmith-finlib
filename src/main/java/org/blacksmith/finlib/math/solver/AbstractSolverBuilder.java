package org.blacksmith.finlib.math.solver;

public abstract class AbstractSolverBuilder implements SolverBuilder{
  public static final double TOLERANCE = 0.000_000_1;
  public static final long ITERATIONS = 10_000;
  protected Function function;
  protected double tolerance = TOLERANCE;
  protected long iterations = ITERATIONS;

  public AbstractSolverBuilder() {
  }

  public AbstractSolverBuilder withFunction(Function function) {
    this.function = function;
    return this;
  }

  public AbstractSolverBuilder withTolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  public AbstractSolverBuilder withIterations(long iterations) {
    this.iterations = iterations;
    return this;
  }
}
