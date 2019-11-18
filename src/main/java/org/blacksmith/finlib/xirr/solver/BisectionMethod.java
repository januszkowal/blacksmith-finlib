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
public class BisectionMethod {
  /** Default tolerance. */
  public static final double TOLERANCE = 0.000_000_1;

  /**
   * Convenience method for getting an instance of a {@link NewtonMethod.Builder}.
   * @return new Builder
   */
  public static Builder builder() {
    return new Builder();
  }

  private final Function function;
  private final double tolerance;
  private final long iterations;

  /**
   * Construct an instance of the NewtonRaphson method for masochists who
   * do not want to use {@link #builder()}.
   * @param function the function
   * @param tolerance the tolerance
   * @param iterations maximum number of iterations
   */
  public BisectionMethod(
      Function function,
      double tolerance,
      long iterations) {
    this.function = function;
    this.tolerance = tolerance;
    this.iterations = iterations;
  }

  public double solve(final double target, final double guess) {
    return new BisectionMethod.BisectionSolver(function, iterations, tolerance).solve(target,guess);
  }


  /**
   * Builder for {@link NewtonMethod} instances.
   */
  public static class Builder extends AbstractSolverBuilder {
    public Builder() {
    }
    public Solver build() {
      return new BisectionSolver(this.function, this.iterations, this.tolerance);
    }
  }

  public static class BisectionSolver extends AbstractSolver implements Solver {

    public BisectionSolver(Function function,
        long maxIterations, double tolerance) {
      super(function, maxIterations, tolerance);
    }

    public double solve(double target, double guess) {
      setGuess(guess);
      double xLeft = -1;
      double xRight = 3;
      int signRight = (int)Math.signum(function.presentValue(xRight));
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setCandidate((xLeft+xRight)/2.0);
        setValue(function.presentValue(this.getCandidate()));
        int signMid = (int)Math.signum(this.getValue());
        if (Math.abs(this.getValue())<TOLERANCE) {
          return this.getCandidate();
        }
        else {
          if (signMid==signRight) {
            xRight = this.getCandidate();
            signRight = signMid;
          }
          else {
            xLeft = this.getCandidate();
          }
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}
