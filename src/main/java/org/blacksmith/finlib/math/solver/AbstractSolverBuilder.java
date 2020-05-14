package org.blacksmith.finlib.math.solver;

import java.util.function.DoubleUnaryOperator;

public abstract class AbstractSolverBuilder<S extends Solver,F extends Function> implements SolverBuilder<S,F> {
  public static final double TOLERANCE = 0.000_000_1;
  public static final long ITERATIONS = 10_000;
  protected F function;
  protected DoubleUnaryOperator derivative;
  protected double tolerance = TOLERANCE;
  protected long iterations = ITERATIONS;

  public AbstractSolverBuilder() {
  }

  @Override
  public AbstractSolverBuilder<S,F> withFunction(F function) {
    this.function = function;
    return this;
  }

  @Override
  public AbstractSolverBuilder<S,F> withTolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  @Override
  public AbstractSolverBuilder<S,F> withIterations(long iterations) {
    this.iterations = iterations;
    return this;
  }
}
