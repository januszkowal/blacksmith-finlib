package org.blacksmith.finlib.math.solver;

import java.util.function.DoubleUnaryOperator;

public abstract class AbstractSolverBuilder<F extends Function,S extends Solver<F>> implements SolverBuilder<F,S> {
  public static final double TOLERANCE = 0.000_000_1;
  public static final long ITERATIONS = 10_000;
  protected F function;
  protected DoubleUnaryOperator derivative;
  protected double tolerance = TOLERANCE;
  protected long iterations = ITERATIONS;

  public AbstractSolverBuilder() {
  }

  @Override
  public AbstractSolverBuilder<F,S> withFunction(F function) {
    this.function = function;
    return this;
  }

  @Override
  public AbstractSolverBuilder<F,S> withTolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  @Override
  public AbstractSolverBuilder<F,S> withIterations(long iterations) {
    this.iterations = iterations;
    return this;
  }
}
