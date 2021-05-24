package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.function.SolverFunction;

public abstract class AbstractSolver<F extends SolverFunction> implements Solver<F> {
  protected final long maxIterations;
  protected final double accuracy;
  protected final boolean breakIfTheSameCandidate;
  protected Double initialCandidate;
  protected F function;
  //Values actualized during iteration
  protected long iterations;
  protected double candidate;
  protected double functionValue;
  protected double priorCandidate;
  protected int priorCandidateCount;

  public AbstractSolver(long maxIterations, double accuracy, boolean breakIfTheSameCandidate) {
    this.maxIterations = maxIterations;
    this.accuracy = accuracy;
    this.breakIfTheSameCandidate = breakIfTheSameCandidate;
  }

  public Double getInitialCandidate() {
    return this.initialCandidate;
  }

  public void setInitialCandidate(double initialCandidate) {
    this.initialCandidate = initialCandidate;
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
    if (!Double.isFinite(this.candidate)) {
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

  public void reset() {
    iterations = 0;
    priorCandidate = Double.MAX_VALUE;
    priorCandidateCount = 0;
  }

  public void nextIteration() {
    iterations++;
  }

  public long getIteractions() {
    return this.iterations;
  }

  public double getAccuracy() {
    return this.accuracy;
  }

  protected boolean isResultDiffLessThanAccuracy() {
    return Math.abs(functionValue) < accuracy;
  }
}
