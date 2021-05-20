package org.blacksmith.finlib.math.xirr;

import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.SolverBuilder;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

public class XirrCalculatorBuilder {
  private SolverBuilder<SolverFunctionDerivative, Solver<SolverFunctionDerivative>> solverBuilder = null;
  private Double guess = null;
  public XirrCalculatorBuilder() {
  }

  public static XirrCalculatorBuilder builder() {
    return new XirrCalculatorBuilder();
  }

  public XirrCalculatorBuilder withSolverBuilder(SolverBuilder<SolverFunctionDerivative, Solver<SolverFunctionDerivative>> solverBuilder) {
    this.solverBuilder = solverBuilder;
    return this;
  }

  public XirrCalculatorBuilder withGuess(double guess) {
    this.guess = guess;
    return this;
  }

  public XirrCalculator build() {
    return new XirrCalculator<>(solverBuilder.build(), guess);
  }
}
