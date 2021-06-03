package org.blacksmith.finlib.math.xirr;

import java.util.List;

import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;
import org.blacksmith.finlib.math.xirr.dto.XirrCashflow;

class XirrSolverFunctionDerivative implements SolverFunctionDerivative {

  private final List<XirrCashflow> xirrCashflows;

  public XirrSolverFunctionDerivative(List<XirrCashflow> xirrCashflows) {
    this.xirrCashflows = xirrCashflows;
  }

  /**
   * Calculates the future value of the investment if it had been subject to the given rate of return.
   *
   * @param rate the rate of return
   * @return the present value of the investment if it had been subject to the given rate of return
   */
  @Override
  public double computeValue(final double rate) {
    return xirrCashflows.stream()
        .mapToDouble(inv -> inv.futureValue(rate))
        .sum();
  }

  @Override
  public int numberOfDerivatives() {
    return 1;
  }

  /**
   * The derivative of the present value under the given rate.
   *
   * @param rate the rate of return
   * @return derivative of the present value under the given rate
   */
  @Override
  public double computeDerivative(int numberOfDerivative, final double rate) {
    return xirrCashflows.stream()
        .mapToDouble(inv -> inv.derivativeValue(rate))
        .sum();
  }
}
