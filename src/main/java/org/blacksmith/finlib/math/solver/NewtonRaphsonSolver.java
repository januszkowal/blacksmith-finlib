package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.solver.function.FunctionValue;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewtonRaphsonSolver extends AbstractSolver<SolverFunctionDerivative>
    implements Solver<SolverFunctionDerivative> {

  private static final Logger log = LoggerFactory.getLogger(NewtonRaphsonSolver.class);
  private double derivativeValue;

  public NewtonRaphsonSolver(long maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    super(maxIterations, tolerance, breakIfTheSameCandidate);
  }

  @Override
  public double solve(SolverFunctionDerivative function, double target, double guess) {
    this.function = function;
    reset();
    setInitialCandidate(guess);
    //x0 = startValue
    setCandidate(guess);
    for (int i = 0; i < this.maxIterations; i++) {
      nextIteration();
      // y0 = compute
      final FunctionValue fv = function.computeValueAndDerivatives(this.getCandidate());
      setFunctionValue(fv.getValue() - target);
      log.debug("i={} result {}", getIterations(), fv);
      if (isResultDiffLessThanTolerance()) {
        return this.getCandidate();
      } else {
        setDerivativeValue(fv.getPartialDerivative(1));
        // x1 = x0 - (y0.value / y0.derivative)
        double x1 = this.getCandidate() - this.getFunctionValue() / this.getDerivativeValue();
        // x1 - x0 <= accuracy
        if (Math.abs(x1 - this.getCandidate()) <= tolerance)
          return this.getCandidate();
        //x0 = x1
        this.setCandidate(x1);
      }
    }
    throw new NonconvergenceException(guess, maxIterations);
  }

  public double inverse(final SolverFunctionDerivative function, double target, double guess) {
    return solve(function, target, guess);
  }

  public double findRoot(final SolverFunctionDerivative function, final double guess) {
    return solve(function, 0, guess);
  }

  @Override
  public Map<String, ?> getStats() {
    return Map.of(
        "maxIterations", getMaxIterations(),
        "initialCandidate", getInitialCandidate(),
        "iterations", getIterations(),
        "tolerance", getTolerance(),
        "candidate", getCandidate(),
        "functionValue", getFunctionValue(),
        "derivativeValue", getDerivativeValue());
  }

  @Override
  public String toString() {
    return '{'
        + "maxIterations=" + getMaxIterations()
        + ", initialCandidate=" + initialCandidate
        + ", iterations=" + this.getIterations()
        + ", tolerance=" + getTolerance()
        + ", candidate=" + this.getCandidate()
        + ", functionValue=" + this.getFunctionValue()
        + ", derivativeValue=" + getDerivativeValue() + '}';
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
}
