package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.exception.NonconvergenceException;
import org.blacksmith.finlib.exception.TooManyEvaluationsException;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BiSectionSolver extends AbstractUnivariateSolver {

  private final boolean breakIfTheSameCandidate;

  public BiSectionSolver(int maxIterations, double tolerance, double minArg, double maxArg, boolean breakIfTheSameCandidate) {
    super(maxIterations, minArg, maxArg, tolerance);
    this.breakIfTheSameCandidate = breakIfTheSameCandidate;
  }

  public Map<String, ?> getStats() {
    return Map.of(
        "maxIterations", getMaxIterations(),
        "minArg", min,
        "maxArg", max,
        "initialCandidate", this.initialCandidate,
        "iterations", getIterations(),
        "tolerance", getTolerance(),
        "candidate", this.getCandidate(),
        "functionValue", getFunctionValue());
  }

  //  protected boolean isResultDiffLessThanTolerance() {
  //    return Math.abs(this.functionValue - target) < tolerance;
  //  }

  @Override
  public String toString() {
    return '{'
        + "maxIterations=" + getMaxIterations()
        + ", initialCandidate=" + initialCandidate
        + ", iterations=" + this.getIterations()
        + ", tolerance=" + getTolerance()
        + ", candidate=" + this.getCandidate()
        + ", functionValue=" + this.getFunctionValue() + '}';
  }

  @Override
  protected double doSolve() throws TooManyEvaluationsException {
    double a = min;
    double b = max;

    double fa = function.value(min) - target;
    while (iterations < this.maxIterations) {
      iterations++;
      setCandidate((a + b) * 0.5);
      setFunctionValue(function.value(getCandidate()) - target);
      log.debug("i={} a={} b={} arg={} fa={} fv={} target={}", iterations, a, b, getCandidate(), fa, getFunctionValue(), target);
      if (getFunctionValue() * fa > 0) {
        a = this.getCandidate();
        fa = getFunctionValue();
      } else {
        b = this.getCandidate();
      }
      if (Math.abs(this.getFunctionValue()) < tolerance) {
        return (a + b) * 0.5;
      }
    }
    throw new NonconvergenceException(this.getInitialCandidate(), maxIterations);
  }

  @Override
  protected void setup(UnivariateFunction function, double target, double startValue) {
    super.setup(function, target, startValue);
  }
}
