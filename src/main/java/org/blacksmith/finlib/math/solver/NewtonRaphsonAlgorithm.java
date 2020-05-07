package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;

/**
 * Simple implementation of the Newton-Raphson method for finding roots or
 * inverses of a function.
 * <p>
 * The function and its derivative must be supplied as instance of Function
 * For examples of usage, see the source of the test class or the Xirr class.
 * If the value of the function at the candidate input is within the <code>tolerance</code> of the desired target value,
 * the method terminates.
 * <p>
 * The <code>iterations</code> parameter is used as an upper bound on the number of iterations to run the method for.
 * <p>
 * The <code>tolerance</code> parameter is used to determine when the method has been successful.
 */
public class NewtonRaphsonAlgorithm {

  private NewtonRaphsonAlgorithm() {
  }
  /**
   * Convenience method for getting an instance of a {@link AlgorithmSolverBuilder}.
   * @return new Builder
   */
  public static AlgorithmSolverBuilder builder() {
    return new AlgorithmSolverBuilder();
  }
  /**
   * Builder for {@link NewtonRaphsonSolver} instances.
   */
  public static class AlgorithmSolverBuilder extends AbstractSolverBuilder {

    public AlgorithmSolverBuilder() {}

    public Solver build() {
      return new NewtonRaphsonSolver(function, this.iterations, this.tolerance);
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
        setFunctionValue(function.functionValue(getArgument()) - target);
        if (Math.abs(this.getFunctionValue()) < this.getTolerance()) {
          return this.getArgument();
        } else {
          setDerivativeValue(function.derivativeValue(getArgument()));
          setArgument(this.getArgument() - this.getFunctionValue() / this.getDerivativeValue());
        }
      }
      throw new NonconvergenceException(guess, maxIterations);
    }
  }
}