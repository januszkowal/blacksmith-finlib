package org.blacksmith.finlib.math.solver;

import java.util.Map;
import java.util.function.Function;

import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;

@Slf4j
public class BisectionSolver extends AbstractSolver<SolverFunction> {

  private final double minArg;
  private final double maxArg;

  public BisectionSolver(Function<Double, Double> argAligner,
      long maxIterations, double tolerance, double minArg, double maxArg, boolean breakIfTheSameCandidate) {
    super(argAligner, maxIterations, tolerance, breakIfTheSameCandidate);
    this.minArg = minArg;
    this.maxArg = maxArg;
  }

  @Override
  public double solve(SolverFunction function, double target, double guess) {
    resetCounter();
    setInitialGuess(guess);
    double a = this.minArg;
    double b = this.maxArg;
    setInitialGuess(guess);
    double fvd = function.value(a) - target;
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setCandidate((a + b) / 2);
      setFunctionValue(function.value(getCandidate()) - target);
      log.debug("i={} arg={} fv={} dv={}", getIterations(), getCandidate(), getFunctionValue(), getDerivativeValue());
      if (isTargetAchieved()) {
        return this.getCandidate();
      } else {
        if (priorCandidateCount > 2 && breakIfTheSameCandidate) {
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
        "initialGuess", getInitialGuess(),
        "iterations", getIterations(),
        "candidate", getCandidate(),
        "functionValue", getFunctionValue(),
        "derivativeValue", getDerivativeValue());
  }
}
