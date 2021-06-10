package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.exception.OverflowException;
import org.blacksmith.finlib.exception.TooManyEvaluationsException;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public abstract class AbstractSolver<FUNC extends UnivariateFunction> {
  protected final int maxIterations;
  protected final double min;
  protected final double max;
  protected final double tolerance;
  protected FUNC function;
  protected double target;
  protected double initialCandidate;
  protected int iterations;
  private double candidate;
  private double priorCandidate;
  private double functionValue;
  private int priorCandidateCount;

  public AbstractSolver(int maxIterations, double min, double max, double tolerance) {
    this.maxIterations = maxIterations;
    this.min = min;
    this.max = max;
    this.tolerance = tolerance;
  }

  public double solve(FUNC function, double target, double candidate)
      throws TooManyEvaluationsException {
    // Initialization.
    setup(function, target, candidate);
    // Perform computation.
    return doSolve();
  }

  public int getMaxIterations() {
    return this.maxIterations;
  }

  public int getIterations() {
    return this.iterations;
  }

  public double getTolerance() {
    return this.tolerance;
  }

  public double getCandidate() {
    return this.candidate;
  }

  public void setCandidate(double newCandidate) {

    //    this.candidate = function.alignCandidate(newCandidate);

    if (!Double.isFinite(newCandidate)) {
      throw new OverflowException("Candidate overflow");
      //      throw new OverflowException("Candidate overflow", this.getStats());
    }
    this.priorCandidate = this.candidate;
    this.candidate = newCandidate;
    if (this.candidate == priorCandidate) {
      this.priorCandidateCount++;
    } else {
      this.priorCandidateCount = 0;
    }
  }

  public double getFunctionValue() {
    return this.functionValue;
  }

  protected void setFunctionValue(double functionValue) {
    this.functionValue = functionValue;
    if (!Double.isFinite(functionValue)) {
      throw new OverflowException("Function value overflow");
    }
  }

  public double getInitialCandidate() {
    return this.initialCandidate;
  }

  protected abstract double doSolve() throws TooManyEvaluationsException;

  protected void setup(FUNC function, double target, double initialCandidate) {
    this.function = function;
    this.target = target;
    this.initialCandidate = initialCandidate;
    //
    this.iterations = 0;
  }
}
