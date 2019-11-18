package org.blacksmith.finlib.xirr;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.xirr.exception.NonconvergenceException;
import org.blacksmith.finlib.xirr.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.xirr.solver.NewtonMethod;
import org.blacksmith.finlib.xirr.solver.SolverBuilder;


import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Calculates the irregular rate of return on a series of transactions.  The
 * irregular rate of return is the constant rate for which, if the transactions
 * had been applied to an investment with that rate, the same resulting returns
 * would be realized.
 * <p>
 * When creating the list of {@link Cashflow} instances to feed Xirr, be
 * sure to include one cashflow representing the present value of the account
 * now, as if you had cashed out the investment.
 * <p>
 * Example usage:
 * <code>
 *     double rate = new Xirr(
 *             new Cashflow(-1000, "2016-01-15"),
 *             new Cashflow(-2500, "2016-02-08"),
 *             new Cashflow(-1000, "2016-04-17"),
 *             new Cashflow( 5050, "2016-08-24")
 *         ).xirr();
 * </code>
 * <p>
 * Example using the builder to gain more control:
 * <code>
 *     double rate = Xirr.builder()
 *         .withNewtonRaphsonBuilder(
 *             NewtonRaphson.builder()
 *                 .withIterations(1000)
 *                 .withTolerance(0.0001))
 *         .withGuess(.20)
 *         .withTransactions(
 *             new Cashflow(-1000, "2016-01-15"),
 *             new Cashflow(-2500, "2016-02-08"),
 *             new Cashflow(-1000, "2016-04-17"),
 *             new Cashflow( 5050, "2016-08-24")
 *         ).xirr();
 * </code>
 * <p>
 * This class is not thread-safe and is designed for each instance to be used
 * once.
 */
public class Xirr implements Function{

  private static final double DAYS_IN_YEAR = 365;

  /**
   * Convenience method for getting an instance of a {@link Builder}.
   * @return new Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  private final List<XirrCashflow> investments;
  private final XirrDetails details;

  private SolverBuilder solverBuilder = null;
  private Double guess = null;

  /**
   * Construct an Xirr instance for the given cashflows.
   * @param csws the cashflows
   * @throws IllegalArgumentException if there are fewer than 2 cashflows
   * @throws IllegalArgumentException if all the cashflows are on the same date
   * @throws IllegalArgumentException if all the cashflows negative (deposits)
   * @throws IllegalArgumentException if all the cashflows non-negative (withdrawals)
   */
  public Xirr(Cashflow... csws) {
    this(Arrays.asList(csws));
  }

  /**
   * Construct an Xirr instance for the given cashflows.
   * @param csws the cashflows
   * @throws IllegalArgumentException if there are fewer than 2 cashflows
   * @throws IllegalArgumentException if all the cashflows are on the same date
   * @throws IllegalArgumentException if all the cashflows negative (deposits)
   * @throws IllegalArgumentException if all the cashflows non-negative (withdrawals)
   */
  public Xirr(Collection<Cashflow> csws) {
    this(csws, null, null);
  }

  public Xirr(Collection<Cashflow> csws, SolverBuilder builder, Double guess) {
    details = csws.stream().collect(XirrDetails.collector());
    details.validate();
    investments =csws.stream()
        .collect(Collectors.groupingBy(Cashflow::getDate, Collectors.summarizingDouble(Cashflow::getAmount)))
        .entrySet().stream().map((k)->createCashflow(k.getKey(),k.getValue().getSum())).collect(Collectors.toList());
    if (investments.size() < 2) {
      throw new IllegalArgumentException(
          "Must have at least two cashflows");
    }
    this.solverBuilder = builder != null ? builder : NewtonMethod.builder();
    this.guess = guess;
  }

  private XirrCashflow createCashflow(LocalDate date, double amount) {
    // Transform the cashflows into an internal representation
    // It is much easier to calculate the present value of an Cashflow
    return new XirrCashflow(amount,
        DAYS.between(date, details.end) / DAYS_IN_YEAR);
  }

  /**
   * Calculates the present value of the investment if it had been subject to
   * the given rate of return.
   * @param rate the rate of return
   * @return the present value of the investment if it had been subject to the
   *         given rate of return
   */
  public double presentValue(final double rate) {
    return investments.stream()
        .mapToDouble(inv -> inv.presentValue(rate))
        .sum();
  }

  /**
   * The derivative of the present value under the given rate.
   * @param rate the rate of return
   * @return derivative of the present value under the given rate
   */
  public double derivative(final double rate) {
    return investments.stream()
        .mapToDouble(inv -> inv.derivative(rate))
        .sum();
  }

  /**
   * Calculates the irregular rate of return of the cashflows for this
   * instance of Xirr.
   * @return the irregular rate of return of the cashflows
   * @throws ZeroValuedDerivativeException if the derivative is 0 while executing the Newton-Raphson method
   * @throws NonconvergenceException if the Newton-Raphson method fails to converge in the
   */
  public double xirr() {
    final double years = DAYS.between(details.start, details.end) / DAYS_IN_YEAR;
    if (details.maxAmount == 0) {
      return -1; // Total loss
    }
    guess = guess != null ? guess : (details.total / details.outcomes) / years;
    return solverBuilder.withFunction(this)
        .build()
        .findRoot(guess);
  }

  /**
   * Builder for {@link Xirr} instances.
   */
  public static class Builder {
    private Collection<Cashflow> cashflows = null;
    private SolverBuilder builder = null;
    private Double guess = null;

    public Builder() {
    }

    public Builder withCashflows(Cashflow... txs) {
      return withCashflows(Arrays.asList(txs));
    }

    public Builder withCashflows(Collection<Cashflow> txs) {
      this.cashflows = txs;
      return this;
    }

    public Builder withSolverBuilder(NewtonMethod.Builder builder) {
      this.builder = builder;
      return this;
    }

    public Builder withGuess(double guess) {
      this.guess = guess;
      return this;
    }

    public Xirr build() {
      return new Xirr(cashflows, builder, guess);
    }
  }

}
