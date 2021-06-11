package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

//public abstract class AbstractSolverBuilder<F extends UnivariateFunction, S extends Solver<F>> implements SolverBuilder<F, S> {
public abstract class AbstractSolverBuilder<F extends UnivariateFunction> implements SolverBuilder<F> {
  public static final double TOLERANCE = 0.000_000_1;
  public static final int MAX_ITERATIONS = 10_000;
  protected double minArg = Double.MIN_VALUE;
  protected double maxArg = Double.MAX_VALUE;
  protected double tolerance = TOLERANCE;
  protected int maxIterations = MAX_ITERATIONS;
  protected boolean breakIfCandidateNotChanging = false;

  public AbstractSolverBuilder() {
  }

  @Override
  public SolverBuilder<F> tolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  @Override
  public SolverBuilder<F> maxIterations(int maxIterations) {
    this.maxIterations = maxIterations;
    return this;
  }

  @Override
  public SolverBuilder<F> minArg(double minArg) {
    this.minArg = minArg;
    return this;
  }

  @Override
  public SolverBuilder<F> maxArg(double maxArg) {
    this.maxArg = maxArg;
    return this;
  }

  @Override
  public SolverBuilder<F> breakIfCandidateNotChanging(boolean breakIfCandidateNotChanging) {
    this.breakIfCandidateNotChanging = breakIfCandidateNotChanging;
    return this;
  }
}
