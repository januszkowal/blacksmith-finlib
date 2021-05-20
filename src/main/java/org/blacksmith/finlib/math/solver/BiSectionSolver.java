package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiSectionSolver extends AbstractSolver<SolverFunctionDerivative> {

  private double derivativeValue;

  public BiSectionSolver(long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    super(maxIterations, tolerance, breakIfTheSameCandidate);
  }

  @Override
  public double solve(SolverFunctionDerivative function, double target, double guess, double minArg, double maxArg) {
    this.function = function;
    resetCounter();
    setInitialGuess(guess);
    double a = minArg;
    double b = maxArg;
    setInitialGuess(guess);
    double fvd = function.getValue(a) - target;
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setCandidate((a + b) / 2);
      setFunctionValue(function.getValue(getCandidate()) - target);
      log.debug("i={} arg={} fv={} dv={}", getIterations(), getCandidate(), getFunctionValue(), getDerivativeValue());
      if (isTargetAchieved()) {
        return this.getCandidate();
      } else {
        if (breakIfTheSameCandidate && priorCandidateCount > 2) {
          return this.getCandidate();
        }
        if (getFunctionValue() * fvd < 0) {
          b = this.getCandidate();
        } else {
          a = this.getCandidate();
          fvd = getFunctionValue();
        }
      }
    }
    throw new NonconvergenceException(guess, maxIterations);
  }

  @Override
  public Map<String, ?> getStats() {
    return Map.of(
        //        "minArg", minArg,
        //        "maxArg", maxArg,
        "initialGuess", getInitialGuess(),
        "iterations", getIterations(),
        "candidate", getCandidate(),
        "functionValue", getFunctionValue(),
        "derivativeValue", getDerivativeValue());
  }

  public double getDerivativeValue() {
    return this.derivativeValue;
  }

  public void setDerivativeValue(double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow", this.getStats());
    } else if (derivativeValue == 0.0d) {
      throw new ZeroValuedDerivativeException(this.getStats());
    }
  }

  @Override
  public String toString() {
    return '{'
        + "initialGuess=" + initialGuess
        + ", iterations=" + this.getIteractions()
        + ", candidate=" + this.getCandidate()
        + ", functionValue=" + this.getFunctionValue()//+targetValue
        + ", derivative=" + derivativeValue + '}';
  }

}
