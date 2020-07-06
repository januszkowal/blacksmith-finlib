package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewtonRaphsonSolver extends AbstractSolver<SolverFunction1stDerivative> {

  private static final Logger log = LoggerFactory.getLogger(NewtonRaphsonSolver.class);

  public NewtonRaphsonSolver(java.util.function.Function<Double, Double> argAligner,
      long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    super(argAligner, maxIterations, tolerance, breakIfTheSameCandidate);
  }

  @Override
  public double solve(SolverFunction1stDerivative function, double target, double guess) {
    resetCounter();
    setInitialGuess(guess);
    setCandidate(guess);
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      setFunctionValue(function.value(getCandidate()) - target);
      log.debug("i={} arg={} fv={} dv={}",
          getIterations(), getCandidate(), getFunctionValue(), getDerivativeValue());
      if (isTargetAchieved()) {
        return this.getCandidate();
      } else {
        if (priorCandidateCount>2 && breakIfTheSameCandidate)
          return this.getCandidate();
        setDerivativeValue(function.derivative(getCandidate()));
        setCandidate(this.getCandidate() - this.getFunctionValue() / this.getDerivativeValue());
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
