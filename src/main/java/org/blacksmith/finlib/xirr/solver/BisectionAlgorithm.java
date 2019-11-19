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
  private BisectionAlgorithm() {
  }
  /**
   * Convenience method for getting an instance of a {@link BisectionAlgorithm.AlgorithmSolverBuilder}.
   * @return new Builder
   */
  public static AlgorithmSolverBuilder builder() {
    return new AlgorithmSolverBuilder();
  }

  /**
   * Builder for {@link BisectionSolver} instances.
   */
  public static class AlgorithmSolverBuilder extends AbstractSolverBuilder {
    private double minArg = -1.0;
    private double maxArg =  2.0;
    public AlgorithmSolverBuilder() {}
    public AlgorithmSolverBuilder withMinArg(double minArg) {
      this.minArg = minArg;
      return this;
    }
    public AlgorithmSolverBuilder withMaxArg(double maxArg) {
      this.maxArg = maxArg;
      return this;
    }
    public Solver build() {
      return new BisectionSolver(this.function, this.iterations, this.tolerance,
          this.minArg, this.maxArg);
    }
  }

  public static class BisectionSolver extends AbstractSolver {

    private double minArg;
    private double maxArg;

    public BisectionSolver(Function function,
        long maxIterations, double tolerance, double minArg, double maxArg) {
      super(function, maxIterations, tolerance);
      this.minArg = minArg;
      this.maxArg = maxArg;
    }

    public double solve(double target, double guess) {
      setInitialGuess(guess);
      double leftArg  = this.minArg;
      double rightArg = this.maxArg;
      System.out.println("left=" + leftArg + " right=" + rightArg);
      int signLeft  = (int)Math.signum(function.presentValue(leftArg));
      int signRight = (int)Math.signum(function.presentValue(rightArg));
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setArgument((leftArg+rightArg)/2.0);
        setFunctionValue(function.presentValue(this.getArgument()) - target);
        int signMid = (int)Math.signum(this.getFunctionValue());
        if (Math.abs(this.getFunctionValue())<this.getTolerance()) {
          return this.getArgument();
        }
        else {
          if (signMid==signRight) {
            rightArg = this.getArgument();
            signRight = signMid;
          }
          else {
            leftArg = this.getArgument();
            signLeft = signMid;
          }
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}
