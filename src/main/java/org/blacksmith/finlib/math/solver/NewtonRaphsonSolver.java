package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.solver.function.FunctionValue;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewtonRaphsonSolver extends AbstractSolver<SolverFunctionDerivative> {

  private static final Logger log = LoggerFactory.getLogger(NewtonRaphsonSolver.class);
  private double derivativeValue;

  public NewtonRaphsonSolver(long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    super(maxIterations, tolerance, breakIfTheSameCandidate);
  }

  public double solve(final SolverFunctionDerivative function, double target, double guess) {
    return solve(function, target, guess, Double.MIN_VALUE, Double.MAX_VALUE);
  }

  public double inverse(final SolverFunctionDerivative function, double target, double guess) {
    return solve(function, target, guess);
  }

  public double findRoot(final SolverFunctionDerivative function, final double guess) {
    return solve(function, 0, guess);
  }

  @Override
  public double solve(SolverFunctionDerivative function, double target, double guess, double minArg, double maxArg) {
    this.function = function;
    resetCounter();
    setInitialGuess(guess);
    setCandidate(guess);
    //    org.apache.commons.math3.analysis.solvers.NewtonRaphsonSolver
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      FunctionValue fv = function.getFunctionValue(this.getCandidate());
      //checkFunctionValue(fv);
      setFunctionValue(fv.getValue() - target);
      log.debug("i={} arg={} fv={} dv={}",
          getIterations(), getCandidate(), getFunctionValue(), getDerivativeValue());
      if (isTargetAchieved()) {
        return this.getCandidate();
      } else {
        setDerivativeValue(fv.getPartialDerivative(1));
        double x1 = this.getCandidate() - this.getFunctionValue() / this.getDerivativeValue();
        if (Math.abs(x1 - this.getCandidate()) <= tolerance)
          return this.getCandidate();
        this.setCandidate(x1);
      }
    }
    /*
    * double x0 = startValue;
        double x1;
        while (true) {
            final DerivativeStructure y0 = computeObjectiveValueAndDerivative(x0);
            x1 = x0 - (y0.getValue() / y0.getPartialDerivative(1));
            if (FastMath.abs(x1 - x0) <= absoluteAccuracy) {
                return x1;
            }

            x0 = x1;
        }
    * */
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

  public double getDerivativeValue() {
    return this.derivativeValue;
  }

  private void setDerivativeValue(double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow", this.getStats());
    } else if (derivativeValue == 0.0d) {
      throw new ZeroValuedDerivativeException(this.getStats());
    }
  }

  private void checkFunctionValue(FunctionValue fv) {
    if (!Double.isFinite(fv.getPartialDerivative(1))) {
      throw new OverflowException("Derivative overflow", this.getStats());
    }
  }
}
