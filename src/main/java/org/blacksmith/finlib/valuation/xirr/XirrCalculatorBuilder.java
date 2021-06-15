package org.blacksmith.finlib.valuation.xirr;

import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.math.solver.Solver;

public class XirrCalculatorBuilder {
  private Double guess = null;
  private DayCount dayCount = StandardDayCounts.ACT_365;
  private Solver<UnivariateFunction> solver;

  public XirrCalculatorBuilder() {
  }

  public static XirrCalculatorBuilder builder() {
    return new XirrCalculatorBuilder();
  }

  public XirrCalculatorBuilder withSolverFunction(Solver<UnivariateFunction> solver) {
    this.solver = solver;
    return this;
  }

  public XirrCalculatorBuilder withSolverFunctionDerivative(Solver<UnivariateDifferentiableFunction> solver) {
    this.solver = castSolver(solver, UnivariateFunction.class);
    return this;
  }

  public XirrCalculatorBuilder withGuess(double guess) {
    this.guess = guess;
    return this;
  }

  public XirrCalculatorBuilder withDayCount(DayCount dayCount) {
    this.dayCount = dayCount;
    return this;
  }

  public XirrCalculator build() {
    return new XirrCalculator(solver, dayCount, guess);
  }

  public static <T extends UnivariateFunction> Solver<T> castSolver(Solver<?> solver, Class<T> cls) {
    return (Solver<T>)solver;
  }


}
