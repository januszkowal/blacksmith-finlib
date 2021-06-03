package org.blacksmith.finlib.math.xirr;

import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.SolverBuilder;
import org.blacksmith.finlib.math.solver.function.SolverFunction;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

public class XirrCalculatorBuilder {
  private Double guess = null;
  private Solver<SolverFunction> solver;
//  private Solver<SolverFunctionDerivative> solver;

  public XirrCalculatorBuilder() {
  }

  public static XirrCalculatorBuilder builder() {
    return new XirrCalculatorBuilder();
  }

  public XirrCalculatorBuilder withSolverBuilder(SolverBuilder<SolverFunctionDerivative, Solver<SolverFunctionDerivative>> solverBuilder) {
//    this.solverBuilder = solverBuilder;
    return this;
  }

//  public XirrCalculatorBuilder withSolver(Solver<SolverFunctionDerivative> solver) {
//    this.solver = solver;
//    return this;
//  }

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
