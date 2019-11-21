package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;

public abstract class AbstractSolver implements Solver {
  protected final Function function;
  protected final long maxIterations;
  protected final double tolerance;
  protected double initialGuess;

  //Values actualized during iteration
  //Current iteration
  private long iteration;
  //Current function argument
  protected double argument;
  //Current function value
  protected double functionValue;
  //Current derivative value
  protected Double derivativeValue;

  public AbstractSolver(Function function, long maxIterations, double tolerance) {
    this.function = function;
    this.maxIterations = maxIterations;
    this.tolerance = tolerance;
  }

  public void setInitialGuess(double initialGuess) {
    this.initialGuess = initialGuess;
  }
  public double getInitialGuess() {
    return this.initialGuess;
  }
  public void nextIteration() {
    iteration++;
  }
  public long getIteration() {
    return this.iteration;
  }
  public double getArgument() {
    return argument;
  }
  public void setArgument(double argument) {
    this.argument = argument;
    if (!Double.isFinite(argument)) {
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
  public double getFunctionValue() {
    return functionValue;
  }
  public void setFunctionValue(double functionValue) {
    this.functionValue = functionValue;
    if (!Double.isFinite(functionValue)) {
      throw new OverflowException("Function value overflow", this);
    }
  }

  @Override
  public String toString() {
    return '{'
        + "initialGuess=" + initialGuess
        + ", iteration="+ iteration
        + ", argument=" + argument
        + ", functionValue=" + functionValue
        + ", derivative=" + derivativeValue + '}';
  }
}
