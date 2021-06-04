package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunction;

public abstract class AbstractSolverBuilder<F extends SolverFunction, S extends Solver<F>> implements SolverBuilder<F, S> {
  public static final double TOLERANCE = 0.000_000_1;
  public static final long MAX_ITERATIONS = 10_000;
  protected double minArg = Double.MIN_VALUE;
  protected double maxArg = Double.MAX_VALUE;
  protected double tolerance = TOLERANCE;
  protected long maxIterations = MAX_ITERATIONS;
  protected boolean breakIfCandidateNotChanging = false;

  public AbstractSolverBuilder() {
  }

  @Override
  public SolverBuilder<F, S> tolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  @Override
  public SolverBuilder<F, S> maxIterations(long maxIterations) {
    this.maxIterations = maxIterations;
    return this;
  }

  @Override
  public SolverBuilder<F, S> minArg(double minArg) {
    this.minArg = minArg;
    return this;
  }

  @Override
  public SolverBuilder<F, S> maxArg(double maxArg) {
    this.maxArg = maxArg;
    return this;
  }

  @Override
  public SolverBuilder<F, S> breakIfCandidateNotChanging(boolean breakIfCandidateNotChanging) {
    this.breakIfCandidateNotChanging = breakIfCandidateNotChanging;
    return this;
  }
}
