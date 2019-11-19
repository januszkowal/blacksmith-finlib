package org.blacksmith.finlib.xirr.solver;

import org.blacksmith.finlib.xirr.Function;
import org.blacksmith.finlib.xirr.exception.NonconvergenceException;

/**
 * Simple implementation of the Newton-Raphson method for finding roots or
 * inverses of a function.
 * <p>
 * The function and its derivative must be supplied as instances of
 * DoubleUnaryOperator and the answers are computed as doubles.
 * <p>
 * For examples of usage, see the source of the test class or the Xirr class.
 * <p>
 * The <code>iterations</code> parameter is used as an upper bound on the number
 * of iterations to run the method for.
 * <p>
 * The <code>tolerance</code> parameter is used to determine when the method
 * has been successful.  If the value of the function at the candidate input
 * is within the <code>tolerance</code> of the desired target value, the
 * method terminates.
 */
public class NewtonRaphsonAlgorithm {
  /** Default tolerance. */
  public static final double TOLERANCE = 0.000_000_1;
  private final Function function;
  private final double tolerance;
  private final long iterations;

  /**
   * Convenience method for getting an instance of a {@link Builder}.
   * @return new Builder
   */
  public static Builder builder() {
    return new Builder();
  }
  /**
   * Construct an instance of the NewtonRaphsonMethod
   * do not want to use {@link #builder()}.
   * @param function the function
   * @param tolerance the tolerance
   * @param iterations maximum number of iterations
   */
  public NewtonRaphsonAlgorithm(
      Function function,
      double tolerance,
      long iterations) {
    this.function = function;
    this.tolerance = tolerance;
    this.iterations = iterations;
  }

  /**
   * Builder for {@link NewtonRaphsonSolver} instances.
   */
  public static class Builder extends AbstractSolverBuilder {

    public Builder() {}

    public Solver build() {
      return new NewtonRaphsonSolver(this.function, this.iterations, this.tolerance);
    }
  }

  public static class NewtonRaphsonSolver extends AbstractSolver {

    public NewtonRaphsonSolver(Function function,
        long maxIterations, double tolerance) {
      super(function, maxIterations, tolerance);
    }

    public double solve(double target, double guess) {
      setInitialGuess(guess);
      setArgument(guess);
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setFunctionValue(function.presentValue(getArgument()) - target);
        if (Math.abs(this.getFunctionValue()) < this.getTolerance()) {
          return this.getArgument();
        } else {
          setDerivativeValue(function.derivative(getArgument()));
          setArgument(this.getArgument() - this.getFunctionValue() / this.getDerivativeValue());
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}
