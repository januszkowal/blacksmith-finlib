package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.exception.NonconvergenceException;
import org.blacksmith.finlib.exception.OverflowException;
import org.blacksmith.finlib.exception.TooManyEvaluationsException;
import org.blacksmith.finlib.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.analysis.DerivativeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewtonRaphsonSolver extends AbstractUnivariateDifferentiableSolver {

  private static final Logger log = LoggerFactory.getLogger(NewtonRaphsonSolver.class);
  int i = 0;
  private double derivativeValue;

  public NewtonRaphsonSolver(int maxIterations, double tolerance, boolean breakIfTheSameCandidate) {
    super(maxIterations, Double.MIN_VALUE, Double.MAX_VALUE, tolerance);
  }

  @Override
  protected double doSolve() throws TooManyEvaluationsException {
//    if (isResultDiffLessThanTolerance()) {
//      return this.getCandidate();
    this.setCandidate(this.initialCandidate);
    while (iterations < this.maxIterations) {
      iterations++;
      final DerivativeValue y0 = function.computeValueAndDerivative(this.getCandidate());
      setFunctionValue(y0.getValue() - target);
//      setFunctionValue(y0.getValue());
      setDerivativeValue(y0.getPartialDerivative(1));
      // x1 = x0 - (y0.value / y0.derivative)
      double x1 = this.getCandidate() - this.getFunctionValue() / this.getDerivativeValue();
      log.debug("i={} x1={} candidate={} result {}, target={}", iterations, x1, this.getCandidate(), y0, target);
      // x1 - x0 <= accuracy
      if (Math.abs(x1 - this.getCandidate()) <= tolerance) {
        return this.getCandidate();
      }
      //x0 = x1
      this.setCandidate(x1);
    }
    throw new NonconvergenceException(this.getInitialCandidate(), maxIterations);
    /**
     * double x0 = startValue;
     *         double x1;
     *         while (true) {
     *             final DerivativeStructure y0 = computeObjectiveValueAndDerivative(x0);
     *             x1 = x0 - (y0.getValue() / y0.getPartialDerivative(1));
     *             if (FastMath.abs(x1 - x0) <= absoluteAccuracy) {
     *                 return x1;
     *             }
     *
     *             x0 = x1;
     *         }
     */
  }

  public Map<String, ?> getStats() {
    return Map.of(
        "maxIterations", getMaxIterations(),
        "initialCandidate", this.initialCandidate,
        "iterations", getIterations(),
        "tolerance", getTolerance(),
        "candidate", this.getCandidate(),
        "functionValue", getFunctionValue(),
        "derivativeValue", getDerivativeValue()
    );
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
//        + ", derivativeValue=" + getDerivativeValue() +
        + '}';
  }

  private void setDerivativeValue(double derivativeValue) {
    this.derivativeValue = derivativeValue;
    if (!Double.isFinite(derivativeValue)) {
      throw new OverflowException("Derivative value overflow");
    } else if (derivativeValue == 0.0d) {
      throw new ZeroValuedDerivativeException();
    }
  }

  public double getDerivativeValue() {
    return this.derivativeValue;
  }
}
