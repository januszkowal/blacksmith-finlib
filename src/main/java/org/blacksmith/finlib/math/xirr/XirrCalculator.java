package org.blacksmith.finlib.math.xirr;

import static java.time.temporal.ChronoUnit.DAYS;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.math.solver.function.SolverFunction;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.xirr.dto.XirrCashflow;
import org.blacksmith.finlib.math.xirr.dto.XirrStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculates the irregular rate of return on a series of transactions.  The irregular rate of return is the constant
 * rate for which, if the transactions had been applied to an investment with that rate, the same resulting returns
 * would be realized.
 * <p>
 * When creating the list of {@link Cashflow} instances to feed Xirr, be sure to include one cashflow representing the
 * present value of the account now, as if you had cashed out the investment.
 * <p>
 * Example usage:
 * <code>
 * double rate = new Xirr( new Cashflow(-1000, "2016-01-15"), new Cashflow(-2500, "2016-02-08"), new Cashflow(-1000,
 * "2016-04-17"), new Cashflow( 5050, "2016-08-24") ).xirr();
 * </code>
 * <p>
 * Example using the builder to gain more control:
 * <code>
 * double rate = Xirr.builder() .withSolverBuilder( NewtonRaphsonAlgorithm.builder() .withIterations(1000)
 * .withTolerance(0.0001)) .withGuess(.20) .withTransactions( new Cashflow(-1000, "2016-01-15"), new Cashflow(-2500,
 * "2016-02-08"), new Cashflow(-1000, "2016-04-17"), new Cashflow( 5050, "2016-08-24") ).xirr();
 * </code>
 * <p>
 * This class is not thread-safe and is designed for each instance to be used once.
 */
public class XirrCalculator<F extends SolverFunction> implements SolverFunctionDerivative {

  private static final Logger log = LoggerFactory.getLogger(XirrCalculator.class);

  private static final double DAYS_IN_YEAR = 365;
  private static final boolean STATS_FROM_GROUPED_CASHFLOWS = true;
  private final Solver<F> solver;

  private List<XirrCashflow> xirrCashflows;
  private XirrStats stats;


  private Double guess;
  private long iterations = 0L;

  @SuppressWarnings("unchecked")
  private F getFunction() {return (F)this;}

  /**
   * Construct an Xirr instance for the given cashflows.
   *
   * @throws IllegalArgumentException if there are fewer than 2 cashflows
   * @throws IllegalArgumentException if all the cashflows are on the same date
   * @throws IllegalArgumentException if all the cashflows negative (deposits)
   * @throws IllegalArgumentException if all the cashflows non-negative (withdrawals)
   */
  public XirrCalculator(Solver<F> solver) {
    this(solver, null);
  }

  public XirrCalculator(Solver<F> solver, Double guess) {
    ArgChecker.notNull(solver, "Solver builder must be not null");
    this.solver = solver;
    this.guess = guess;
  }

  public List<Cashflow> groupCashflows(Collection<Cashflow> cashflows) {
    return cashflows.stream()
        .collect(Collectors.groupingBy(Cashflow::getDate, Collectors.summingDouble(Cashflow::getAmount)))
        .entrySet().stream()
        .map(e -> Cashflow.of(e.getKey(), e.getValue()))
        .sorted(Comparator.comparing(Cashflow::getDate))
        .collect(Collectors.toList());
  }

  private XirrCashflow createXirrCashflow(Cashflow cashflow) {
    return new XirrCashflow(cashflow.getAmount(),
        DAYS.between(cashflow.getDate(), stats.getEndDate()) / DAYS_IN_YEAR);
  }

  /**
   * Calculates the future value of the investment if it had been subject to the given rate of return.
   *
   * @param rate the rate of return
   * @return the present value of the investment if it had been subject to the given rate of return
   */
  public double getValue(final double rate) {
    return xirrCashflows.stream()
        .mapToDouble(inv -> inv.futureValue(rate))
        .sum();
  }

  /**
   * The derivative of the present value under the given rate.
   *
   * @param rate the rate of return
   * @return derivative of the present value under the given rate
   */
  public double derivative(final double rate) {
    return xirrCashflows.stream()
        .mapToDouble(inv -> inv.derivative(rate))
        .sum();
  }

  /**
   * Calculates the irregular rate of return of the cashflows for this instance of Xirr.
   * @param cashflows the cashflows
   * @return the irregular rate of return of the cashflows
   * @throws ZeroValuedDerivativeException if the derivative is 0 while executing the Newton-Raphson method
   * @throws NonconvergenceException       if the Newton-Raphson method fails to converge in the
   */
  public double xirr(List<Cashflow> cashflows) {
    ArgChecker.notEmpty(cashflows, "Cashflows must be not empty");
    List<Cashflow> groupedCashflows = groupCashflows(cashflows);
    List<Cashflow> statsCashflows = STATS_FROM_GROUPED_CASHFLOWS ? groupedCashflows : cashflows;
    stats = XirrStats.fromCashflows(statsCashflows);
    this.xirrCashflows = groupedCashflows.stream()
        .map(this::createXirrCashflow)
        .collect(Collectors.toList());
    if (xirrCashflows.size() < 2) {
      throw new IllegalArgumentException("Must have at least two dates");
    }
    stats.validate();

    final double years = DAYS.between(stats.getStartDate(), stats.getEndDate()) / DAYS_IN_YEAR;
    if (stats.getMaxAmount() == 0) {
      return -1; // Total loss
    }
    if (guess == null) {
      guess = (stats.getTotal() / stats.getOutcomes()) / years;
    }

    log.debug("Total={} Incomes={} Outcomes={}", stats.getTotal(), stats.getIncomes(), stats.getOutcomes());

    double xirr;
    try {
      log.debug("Start with Guess={}", guess);
      xirr = solver.findRoot(getFunction(),guess, -1d, 2d);
      this.iterations = solver.getIterations();
      log.debug("Completed after iterations={}", iterations);
    } catch (OverflowException oe) {
      log.warn("Guess sign changed due to overflow,{}", solver.getStats());
      this.iterations = solver.getIterations();
      log.debug("Start with Guess={}", guess);
      xirr = solver.findRoot(getFunction(),-guess, -1d, 2d);
      this.iterations += solver.getIterations();
      log.debug("Completed after iterations={}", iterations);
    }
    return xirr;
  }

  public long getIterations() {
    return this.iterations;
  }
}
