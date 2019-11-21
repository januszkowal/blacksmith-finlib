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

    private double a;
    private double b;

    public BisectionSolver(Function function,
        long maxIterations, double tolerance, double a, double b) {
      super(function, maxIterations, tolerance);
      this.a = a;
      this.b = b;
    }

    public double solve(double target, double guess) {
      setInitialGuess(guess);
      double a  = this.a;
      double b = this.b;
      double aT = function.functionValue(a) - target;
      for (int i = 0; i < this.maxIterations; i++) {
        nextIteration();
        setArgument((a+b)/2.0);
        setFunctionValue(function.functionValue(this.getArgument()));
        double functionValueT = this.getFunctionValue()-target;
        if (Math.abs(functionValueT)<this.getTolerance()) {
          return this.getArgument();
        }
        else {
          if (functionValueT*aT<0) {
            b = this.getArgument();
          }
          else {
            a = this.getArgument();
            aT = functionValueT;
          }
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}
