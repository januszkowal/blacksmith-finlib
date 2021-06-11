package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.exception.OverflowException;

public abstract class AbstractSolver1<F extends UnivariateFunction>  {
  protected final long maxIterations;
  protected final double tolerance;
  protected final boolean breakIfTheSameCandidate;
  protected Double initialCandidate;
  protected F function;
  //Values actualized during iteration
  protected long iterations;
  protected double candidate;
  protected double functionValue;
  protected double priorCandidate;
  protected int priorCandidateCount;

  public AbstractSolver1(long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
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
//    this.candidate = function.alignCandidate(newCandidate);
    this.candidate = newCandidate;
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

  public double getTolerance() {
    return this.tolerance;
  }

  public abstract Map<String, ?> getStats();

  protected boolean isResultDiffLessThanTolerance() {
    return Math.abs(functionValue) < tolerance;
  }
}
