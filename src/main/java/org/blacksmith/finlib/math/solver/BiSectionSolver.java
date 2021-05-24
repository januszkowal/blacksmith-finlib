package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiSectionSolver extends AbstractSolver<SolverFunctionDerivative> {

  private final double minArg;
  private final double maxArg;

  public BiSectionSolver(long maxIterations, double tolerance, boolean breakIfTheSameCandidate, double minArg, double maxArg) {
    super(maxIterations, tolerance, breakIfTheSameCandidate);
    this.minArg = minArg;
    this.maxArg = maxArg;
  }

  @Override
  public double solve(SolverFunctionDerivative function, double target, double guess) {
    this.function = function;
    reset();
    double a = minArg;
    double b = maxArg;
    setInitialCandidate(a);
    setCandidate(a);
    double fvd = function.computeValue(minArg) - target;
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setCandidate((a + b) / 2);
      setFunctionValue(function.computeValue(getCandidate()) - target);
      log.debug("i={} arg={} fv={}", getIterations(), getCandidate(), getFunctionValue());
      if (isResultDiffLessThanTolerance()) {
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
        "minArg", minArg,
        "maxArg", maxArg,
        "initialCandidate", getInitialCandidate(),
        "iterations", getIterations(),
        "candidate", getCandidate(),
        "functionValue", getFunctionValue());
  }

  @Override
  public String toString() {
    return '{'
        + "initialCandidate=" + initialCandidate
        + ", iterations=" + this.getIteractions()
        + ", candidate=" + this.getCandidate()
        + ", functionValue=" + this.getFunctionValue() + '}';
  }

}
