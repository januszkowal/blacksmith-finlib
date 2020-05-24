package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;

public abstract class AbstractSolver<F extends Function> implements Solver<F> {
  protected final F function;
  protected final long maxIterations;
  protected final double tolerance;
  protected Double initialGuess;

  //Values actualized during iteration
  //Current iteration
  private long iterations;
  //Current function argument
  protected double candidate;
  //Current function value
  protected double functionValue;
  //Current derivative value
  protected double derivativeValue;

  public AbstractSolver(F function,
      long maxIterations, double tolerance) {
    this.function = function;
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
  }

  public void setInitialGuess(double initialGuess) {
    this.initialGuess = initialGuess;
  }
  public Double getInitialGuess() {
    return this.initialGuess;
  }
  public void resetCounter() {
    iterations=0;
  }
  public void nextIteration() {
    iterations++;
  }
  public long getIterations() {
    return this.iterations;
  }
  public double getCandidate() {
    return candidate;
  }
  public void setCandidate(double candidate) {
    this.candidate = candidate;
    if (!Double.isFinite(candidate)) {
      throw new OverflowException("Candidate overflow", this.getStats());
    }
  }
  public Double getDerivativeValue() {
    return derivativeValue;
  }

  public long getMaxIterations() {
    return this.maxIterations;
  }

  public double getTolerance() {
    return this.tolerance;
  }

  public boolean isTargetAchieved() {
    return Math.abs(functionValue)<tolerance;
  }

  public void setDerivativeValue(double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow", this.getStats());
    } else if (derivativeValue == 0.0) {
      throw new ZeroValuedDerivativeException(this.getStats());
    }
  }
  public double getFunctionValue() {
    return functionValue;
  }

  public void setFunctionValue(double functionValue) {
    this.functionValue = functionValue;
    if (!Double.isFinite(functionValue)) {
      throw new OverflowException("Function value overflow", this.getStats());
    }
  }

  @Override
  public String toString() {
    return '{'
        + "initialGuess=" + initialGuess
        + ", iterations="+ iterations
        + ", candidate=" + candidate
        + ", functionValue=" + functionValue
        + ", derivative=" + derivativeValue + '}';
  }
}
