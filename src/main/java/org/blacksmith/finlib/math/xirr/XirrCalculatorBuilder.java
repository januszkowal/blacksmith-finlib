package org.blacksmith.finlib.math.xirr;

import java.util.Collection;
import org.blacksmith.finlib.math.solver.Function;
import org.blacksmith.finlib.math.solver.SolverBuilder;

public class XirrCalculatorBuilder<F extends Function> {
  public static <F extends Function> XirrCalculatorBuilder<F> builder() {
    return new XirrCalculatorBuilder<>();
  }

  private Collection<Cashflow> cashflows = null;
  private SolverBuilder<F,?> solverBuilder = null;
  private Double guess = null;

  public XirrCalculatorBuilder() {
  }

  public XirrCalculatorBuilder<F> withCashflows(Collection<Cashflow> txs) {
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
    return new XirrCalculator<>(cashflows, solverBuilder, guess);
  }
}
