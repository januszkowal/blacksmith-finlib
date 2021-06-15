package org.blacksmith.finlib.valuation.xirr;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.datetime.daycount.StandardDayCounts;
import org.blacksmith.finlib.exception.NonconvergenceException;
import org.blacksmith.finlib.exception.OverflowException;
import org.blacksmith.finlib.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.valuation.dto.Cashflow;
import org.blacksmith.finlib.valuation.dto.CashflowAggregator;
import org.blacksmith.finlib.valuation.xirr.dto.XirrCashflow;
import org.blacksmith.finlib.valuation.xirr.dto.XirrStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculates the irregular rate of return on a series of transactions.  The irregular rate of return is the constant
 * rate for which, if the transactions had been applied to an investment with that rate, the same resulting returns
 * would be realized.
 * <p>
 * This class is not thread-safe.
 */
public class XirrCalculator {

  private static final Logger log = LoggerFactory.getLogger(XirrCalculator.class);

  private static final boolean STATS_FROM_GROUPED_CASHFLOWS = true;
  private final DayCount dayCount = StandardDayCounts.ACT_365;
  private final Solver<UnivariateFunction> solver;
  private XirrStats stats;

  private Double guess;
  private long allIterations = 0L;
  private long lastIterations = 0L;

  public XirrCalculator(Solver<UnivariateFunction> solver, Double guess) {
    ArgChecker.notNull(solver, "Solver must be not null");
    this.solver = solver;
    this.guess = guess;
  }

  /**
   * Construct an Xirr instance for the given cashflows.
   *
   * @throws IllegalArgumentException if there are fewer than 2 cashflows
   * @throws IllegalArgumentException if all the cashflows are on the same date
   * @throws IllegalArgumentException if all the cashflows negative (deposits)
   * @throws IllegalArgumentException if all the cashflows non-negative (withdrawals)
   */
  public static XirrCalculator of(Solver<UnivariateFunction> solver) {
    return new XirrCalculator(solver, null);
  }

  /**
   * Calculates the irregular rate of return of the cashflows for this instance of Xirr.
   *
   * @param cashflows the cashflows
   * @return the irregular rate of return of the cashflows
   * @throws ZeroValuedDerivativeException if the derivative is 0 while executing the Newton-Raphson method
   * @throws NonconvergenceException       if the Newton-Raphson method fails to converge in the
   */
  public double xirr(List<Cashflow> cashflows) {
    ArgChecker.notEmpty(cashflows, "Cashflows must be not empty");
    reset();
    List<Cashflow> groupedCashflows = CashflowAggregator.aggregate(cashflows, false);
    List<Cashflow> statsCashflows = STATS_FROM_GROUPED_CASHFLOWS ? groupedCashflows : cashflows;
    stats = XirrStats.fromCashflows(statsCashflows);
    var xirrCashflows = groupedCashflows.stream()
        .map(this::createXirrCashflow)
        .collect(Collectors.toList());
    if (xirrCashflows.size() < 2) {
      throw new IllegalArgumentException("Must have at least two dates");
    }
    stats.validate();
    var derivativeFunction = new XirrFunctionDerivative(xirrCashflows);

    final double years = dayCount.yearFraction(stats.getStartDate(), stats.getEndDate());
    if (stats.getMaxAmount() == 0) {
      return -1; // Total loss
    }
    if (guess == null) {
      guess = (stats.getTotal() / stats.getOutcomes()) / years;
    }

    log.debug("Total={} Incomes={} Outcomes={}", stats.getTotal(), stats.getIncomes(), stats.getOutcomes());

    double xirr = 0.0d;
    log.debug("Start");
    try {
      if (guess == null) {
        guess = (stats.getTotal() / stats.getOutcomes()) / years;
      }
      log.debug("Calculate 1st with Guess={}", guess);
      xirr = calculateInternal(derivativeFunction, guess);
      log.debug("Completed after iterations={}", lastIterations);
      return xirr;
    } catch (NonconvergenceException | OverflowException oe) {
      log.warn("Overflow exception");
    }

    try {
      guess = -(stats.getTotal() / stats.getIncomes()) / years;
      log.debug("Calculate 2nd with Guess={}", guess);
      xirr = calculateInternal(derivativeFunction, guess);
      log.debug("Completed after iterations={}", lastIterations);
      return xirr;
    } catch (NonconvergenceException | OverflowException oe) {
      log.warn("Overflow exception");
    }
    return xirr;
  }

  public long getIterations() {
    return this.allIterations;
  }

  private double calculateInternal(XirrFunctionDerivative derivativeFunction, double guess) {
    try {
      return solver.findRoot(derivativeFunction, guess);
    } finally {
      this.lastIterations = solver.getIterations();
      this.allIterations += this.lastIterations;
    }
  }

  private XirrCashflow createXirrCashflow(Cashflow cashflow) {
    return new XirrCashflow(cashflow.getAmount().doubleValue(), dayCount.yearFraction(cashflow.getDate(), stats.getEndDate()));
  }

  private void reset() {
    this.allIterations = 0L;
    this.lastIterations = 0L;
  }
}
