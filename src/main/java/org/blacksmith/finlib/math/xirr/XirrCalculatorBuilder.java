package org.blacksmith.finlib.math.xirr;

import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.function.SolverFunction;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

public class XirrCalculatorBuilder {
  private Double guess = null;
  private Solver<SolverFunction> solver;

  public XirrCalculatorBuilder() {
  }

  public static XirrCalculatorBuilder builder() {
    return new XirrCalculatorBuilder();
  }

  public XirrCalculatorBuilder withSolverFunction(Solver<SolverFunction> solver) {
    this.solver = solver;
    return this;
  }

  public XirrCalculatorBuilder withSolverFunctionDerivative(Solver<SolverFunctionDerivative> solver) {
    this.solver = castSolver(solver, SolverFunction.class);
    return this;
  }

  public XirrCalculatorBuilder withGuess(double guess) {
    this.guess = guess;
    return this;
  }

  public XirrCalculator build() {
    return new XirrCalculator(solver, guess);
  }

  public static <T extends SolverFunction> Solver<T> castSolver(Solver<?> solver, Class<T> cls) {
    return (Solver<T>)solver;
  }


}
