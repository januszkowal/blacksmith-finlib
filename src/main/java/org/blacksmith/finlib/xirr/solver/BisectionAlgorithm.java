package org.blacksmith.finlib.xirr.solver;

import org.blacksmith.finlib.xirr.Function;
import org.blacksmith.finlib.xirr.exception.NonconvergenceException;

/**
 * Simple implementation of the Bisection method for finding roots or
 * inverses of a function.
 * <p>
 * The function must be supplied as instances of
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
public class BisectionAlgorithm {
  /** Default tolerance. */
  public static final double TOLERANCE = 0.000_000_1;
  private final Function function;
  private final double tolerance;
  private final long iterations;

  /**
   * Convenience method for getting an instance of a {@link BisectionAlgorithm.Builder}.
   * @return new Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Construct an instance of the BisectionMethod
   * do not want to use {@link #builder()}.
   * @param function the function
   * @param tolerance the tolerance
   * @param iterations maximum number of iterations
   */
  public BisectionAlgorithm(
      Function function,
      double tolerance,
      long iterations) {
    this.function = function;
    this.tolerance = tolerance;
    this.iterations = iterations;
  }

  /**
   * Builder for {@link BisectionSolver} instances.
   */
  public static class Builder extends AbstractSolverBuilder {
    public Builder() {}
    public Solver build() {
      return new BisectionSolver(this.function, this.iterations, this.tolerance);
    }
  }

  public static class BisectionSolver extends AbstractSolver {

    public BisectionSolver(Function function,
        long maxIterations, double tolerance) {
      super(function, maxIterations, tolerance);
    }

    public double solve(double target, double guess) {
      setInitialGuess(guess);
      double leftArg = -1;
      double rightArg = 2;
      int signRight = (int)Math.signum(function.presentValue(rightArg));
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setArgument((leftArg+rightArg)/2.0);
        setFunctionValue(function.presentValue(this.getArgument()) - target);
        int signMid = (int)Math.signum(this.getFunctionValue());
        if (Math.abs(this.getFunctionValue())<TOLERANCE) {
          return this.getArgument();
        }
        else {
          if (signMid==signRight) {
            rightArg = this.getArgument();
            signRight = signMid;
          }
          else {
            leftArg = this.getArgument();
          }
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}