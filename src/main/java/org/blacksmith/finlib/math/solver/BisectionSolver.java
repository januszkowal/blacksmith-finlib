package org.blacksmith.finlib.math.solver;

import java.util.Map;
import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;

public class BisectionSolver extends AbstractSolver<Function> {

  private final double minArg;
  private final double maxArg;

  public BisectionSolver(Function function,
      long maxIterations, double tolerance, double minArg, double maxArg) {
    super(function, maxIterations, tolerance);
    this.minArg = minArg;
    this.maxArg = maxArg;
  }

  @Override
  public double solve(double target, double guess) {
    resetCounter();
    setInitialGuess(guess);
    double a = this.minArg;
    double b = this.maxArg;
    setInitialGuess(guess);
    double fvd = function.value(a) - target;
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setCandidate((a + b) / 2.0);
      setFunctionValue(function.value(getCandidate()) - target);
      if (isTargetAchieved()) {
        return this.getCandidate();
      } else {
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
        "iterations",getIterations(),
        "candidate",getCandidate(),
        "functionValue",getFunctionValue(),
        "derivativeValue",getDerivativeValue());
  }
}
