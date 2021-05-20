package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.function.SolverFunction;

public abstract class AbstractSolver<F extends SolverFunction> implements Solver<F> {
  protected final long maxIterations;
  protected final double tolerance;
  protected final boolean breakIfTheSameCandidate;
  protected Double initialGuess;
  protected double priorCandidate = Double.MAX_VALUE;
  protected int priorCandidateCount = 0;
  protected F function;
  //Values actualized during iteration
  //Current iteration
  private long iterations;
  //Current function argument
  private double candidate;
  //Current function value
  private double functionValue;

  public AbstractSolver(long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
    this.breakIfTheSameCandidate = breakIfTheSameCandidate;
  }

  public Double getInitialGuess() {
    return this.initialGuess;
  }

  public void setInitialGuess(double initialGuess) {
    this.initialGuess = initialGuess;
  }

  public long getMaxIterations() {
    return this.maxIterations;
  }

  public long getIterations() {
    return this.iterations;
  }

  public double getCandidate() {
    return candidate;
  }

  protected void setCandidate(double newCandidate) {
    this.priorCandidate = this.candidate;
    this.candidate = function.alignCandidate(newCandidate);
    if (this.candidate == priorCandidate) {
      this.priorCandidateCount++;
    } else {
      this.priorCandidateCount = 0;
    }

    if (!Double.isFinite(candidate)) {
      throw new OverflowException("Candidate overflow", this.getStats());
    }
  }

  public double getFunctionValue() {
    return functionValue;
  }

  protected void setFunctionValue(double functionValue) {
    this.functionValue = functionValue;
    if (!Double.isFinite(functionValue)) {
      throw new OverflowException("Function value overflow", this.getStats());
    }
  }

  public void resetCounter() {
    iterations = 0;
  }

  public void nextIteration() {
    iterations++;
  }

  public long getIteractions() {
    return this.iterations;
  }

  public double getTolerance() {
    return this.tolerance;
  }

  protected boolean isTargetAchieved() {
    return Math.abs(functionValue) < tolerance;
  }
}
