package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;

/**
 * Simple implementation of the Bisection method for finding roots or
 * inverses of a function.
 * <p>
 * The function and its derivative must be supplied as instance of Function
 * For examples of usage, see the source of the test class or the Xirr class.
 * The algorithm consists of repeatedly bisecting the interval defined within range: a <= arg <= b
 * If the value of the function at the candidate input is within the <code>tolerance</code> of the desired target value, the
 * method terminates.
 * <p>
 * The <code>iterations</code> parameter is used as an upper bound on the number of iterations to run the method for.
 * <p>
 * The <code>tolerance</code> parameter is used to determine when the method has been successful.
 * <p>
 * The <code>minArg</code> parameter is used to determine left starting point of the algorithm
 * <p>
 * The <code>maxArg</code> parameter is used to determine right starting point of the algorithm
 */
public class BiSectionAlgorithm {
  private BiSectionAlgorithm() {
  }
  /**
   * Convenience method for getting an instance of a {@link BiSectionAlgorithm.AlgorithmSolverBuilder}.
   * @return new Builder
   */
  public static AlgorithmSolverBuilder builder() {
    return new AlgorithmSolverBuilder();
  }

  /**
   * Builder for {@link BisectionSolver} instances.
   */
  public static class AlgorithmSolverBuilder extends AbstractSolverBuilder {
    private double minArg = Double.MIN_VALUE;
    private double maxArg = Double.MAX_VALUE;
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

    private final double minArg;
    private final double maxArg;

    public BisectionSolver(Function function,
        long maxIterations, double tolerance, double minArg, double maxArg) {
      super(function, maxIterations, tolerance);
      this.minArg = minArg;
      this.maxArg = maxArg;
    }

    public double solve(double target, double guess) {
      setInitialGuess(guess);
      double a = this.minArg;
      double b = this.maxArg;
      setInitialGuess(guess);
      double fvd = function.functionValue(a) - target;
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setArgument((a+b)/2.0);
        setFunctionValue(function.functionValue(getArgument()) - target);
        if (isTargetAchieved()) {
          return this.getArgument();
        }
        else {
          if (getFunctionValue()*fvd<0) {
            b = this.getArgument();
          }
          else {
            a = this.getArgument();
            fvd = getFunctionValue();
          }
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}
