package org.blacksmith.finlib.math.solver;

import java.util.function.Function;

import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;

public abstract class AbstractSolver<F extends SolverFunction> implements Solver<F> {
  protected final long maxIterations;
  protected final double tolerance;
  protected final Function<Double, Double> argAligner;
  protected final boolean breakIfTheSameCandidate;
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

  protected double priorCandidate = Double.MAX_VALUE;
  protected int priorCandidateCount=0;

  public AbstractSolver(Function<Double, Double> argAligner,
      long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    this.argAligner = argAligner;
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
    this.breakIfTheSameCandidate = breakIfTheSameCandidate;
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
  public void setCandidate(double newCandidate) {
    this.priorCandidate = this.candidate;
    this.candidate = argAligner.apply(newCandidate);
    if (this.candidate==priorCandidate) {
      this.priorCandidateCount++;
    }
    else {
      this.priorCandidateCount=0;
    }

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

  public double getTolerance() { return this.tolerance; }

  public boolean isTargetAchieved() {
    return Math.abs(functionValue)<tolerance;
  }

  public void setDerivativeValue(double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow", this.getStats());
    } else if (derivativeValue == 0.0d) {
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
