package org.blacksmith.finlib.math.solver;

import java.util.Map;
import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.blacksmith.finlib.math.solver.function.SolverFunction;

public interface Solver<F extends SolverFunction> {
  double solve(final F function, double target, double guess, double min, double max);
  /**
   * Find the input value to the function which yields the given
   * <code>target</code>, starting at the <code>guess</code>.  More precisely,
   * finds an input value <i>x</i>
   * such that |<i>f</i>(<i>x</i>) - <code>target</code>| &lt; <i>tolerance</i>
   * @param target the target value of the function
   * @param guess value to start the algorithm with
   * @return the inverse of the function at <code>target</code> within the
   * given tolerance
   * @throws ZeroValuedDerivativeException if the derivative is 0 while
   *                                       executing the Newton-Raphson method
   * @throws OverflowException when a value involved is infinite or NaN
   * @throws NonconvergenceException if the method fails to converge in the
   *                                 given number of iterations
   */
  default double inverse(final F function, double target, double guess, double min, double max) {
    return solve(function, target, guess, min, max);
  }
  /**
   * Equivalent to <code>inverse(0, guess)</code>.
   * <p>
   * Find a root of the function starting at the given guess.  Equivalent to
   * invoking <code>inverse(0, guess)</code>.  Finds the input value <i>x</i>
   * such that |<i>f</i>(<i>x</i>)| &lt; <i>tolerance</i>.
   * @param guess the value to start at
   * @return an input to the function which yields zero within the given
   *         tolerance
   * @see #inverse(F, double, double, double, double)
   */
  default double findRoot(final F function, final double guess, double min, double max) {
    return solve(function, 0 , guess, min, max);
  }

  Double getInitialGuess();
  long getMaxIterations();
  long getIterations();
  double getCandidate();
  double getFunctionValue();
  Map<String,?> getStats();
}
