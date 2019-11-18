package org.blacksmith.finlib.xirr.solver;

import org.blacksmith.finlib.xirr.Function;
import org.blacksmith.finlib.xirr.exception.OverflowException;
import org.blacksmith.finlib.xirr.exception.ZeroValuedDerivativeException;

public abstract class AbstractSolver {
  protected final Function function;
  protected final long maxIterations;
  protected final double tolerance;
  private long iteration;
  protected double guess;
  protected double candidate;
  protected double value;
  protected Double derivativeValue;

  public AbstractSolver(Function function, long maxIterations, double tolerance) {
    this.function = function;
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
  }

  public void setGuess(double guess) {
    this.guess = guess;
  }
  public double getGuess() {
    return this.guess;
  }
  public void nextIteration() {
    iteration++;
  }
  public long getIteration() {
    return this.iteration;
  }
  public double getCandidate() {
    return candidate;
  }
  public void setCandidate(double candidate) {
    this.candidate = candidate;
    if (!Double.isFinite(candidate)) {
      throw new OverflowException("Candidate overflow", this);
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

  public void setDerivativeValue(Double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow", this);
    } else if (derivativeValue == 0.0) {
      throw new ZeroValuedDerivativeException(this);
    }
  }
  public double getValue() {
    return value;
  }
  public void setValue(double value) {
    this.value = value;
    if (!Double.isFinite(value)) {
      throw new OverflowException("Function value overflow", this);
    }
  }

  @Override
  public String toString() {
    return '{'
        + "guess=" + getGuess()
        + ", iteration="+ getIteration()
        + ", candidate=" + getCandidate()
        + ", value=" + getValue()
        + ", derivative=" + derivativeValue + '}';
  }
}
