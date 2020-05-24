package org.blacksmith.finlib.math.solver;

import java.util.Map;
import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewtonRaphsonSolver extends AbstractSolver<Function1stDerivative> {

  private static final Logger log = LoggerFactory.getLogger(NewtonRaphsonSolver.class);

  public NewtonRaphsonSolver(Function1stDerivative function, long maxIterations, double tolerance) {
    super(function, maxIterations, tolerance);
  }

  @Override
  public double solve(double target, double guess) {
    resetCounter();
    setInitialGuess(guess);
    setCandidate(guess);
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setFunctionValue(function.value(getCandidate()) - target);
      if (isTargetAchieved()) {
        log.debug("i={} arg={} fv={} OK", getIterations(), getCandidate(), getFunctionValue());
        return this.getCandidate();
      } else {
        setDerivativeValue(function.derivative(getCandidate()));
        setCandidate(this.getCandidate() - this.getFunctionValue() / this.getDerivativeValue());
        log.debug("i={} arg={} fv={} dv={}", getIterations(), getCandidate(), getFunctionValue(), getDerivativeValue());
      }
    }
    throw new NonconvergenceException(guess, maxIterations);
  }

  @Override
  public Map<String, ?> getStats() {
    return Map.of(
        "initialGuess", getInitialGuess(),
        "iterations", getIterations(),
        "candidate", getCandidate(),
        "functionValue", getFunctionValue(),
        "derivativeValue", getDerivativeValue());
  }
}
