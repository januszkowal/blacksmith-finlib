package org.blacksmith.finlib.math.xirr;

import java.util.List;
import org.blacksmith.finlib.math.solver.Function;
import org.blacksmith.finlib.math.solver.SolverBuilder;

public class XirrCalculatorBuilder<F extends Function> {
  public static <F extends Function> XirrCalculatorBuilder<F> builder() {
    return new XirrCalculatorBuilder<>();
  }

  private List<Cashflow> cashflows = null;
  private SolverBuilder<F,?> solverBuilder = null;
  private Double guess = null;

  public XirrCalculatorBuilder() {
  }

  public XirrCalculatorBuilder<F> withCashflows(List<Cashflow> txs) {
    this.cashflows = txs;
    return this;
  }

  public XirrCalculatorBuilder<F> withSolverBuilder(SolverBuilder<F,?> solverBuilder) {
    this.solverBuilder = solverBuilder;
    return this;
  }

  public XirrCalculatorBuilder<F> withGuess(double guess) {
    this.guess = guess;
    return this;
  }

  public XirrCalculator<F> build() {
    return new XirrCalculator<>(cashflows, solverBuilder.build(), guess);
  }
}
