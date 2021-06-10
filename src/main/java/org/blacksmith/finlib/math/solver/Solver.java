package org.blacksmith.finlib.math.solver;

import java.util.Map;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public interface Solver<FUNC extends UnivariateFunction> {
  double solve(final FUNC function, double target, double guess);

  /**
   * Find the input value to the function which yields the given
   * <code>target</code>, starting at the <code>guess</code>.  More precisely,
   * finds an input value <i>x</i>
   * such that |<i>f</i>(<i>x</i>) - <code>target</code>| &lt; <i>tolerance</i>
   *
   * @param target the target value of the function
   * @param guess  value to start the algorithm with
   * @return the inverse of the function at <code>target</code> within the
   * given tolerance
   * @throws ZeroValuedDerivativeException if the derivative is 0 while
   *                                       executing the Newton-Raphson method
   * @throws OverflowException             when a value involved is infinite or NaN
   * @throws NonconvergenceException       if the method fails to converge in the
   *                                       given number of iterations
   */
  default double inverse(final FUNC function, double target, double guess) {
    return solve(function, target, guess);
  }

  /**
   * Equivalent to <code>inverse(0, guess)</code>.
   * <p>
   * Find a root of the function starting at the given guess.  Equivalent to
   * invoking <code>inverse(0, guess)</code>.  Finds the input value <i>x</i>
   * such that |<i>f</i>(<i>x</i>)| &lt; <i>tolerance</i>.
   *
   * @param guess the value to start at
   * @return an input to the function which yields zero within the given
   * tolerance
   * @see #inverse(FUNC, double, double)
   */
  default double findRoot(final FUNC function, final double guess) {
    return solve(function, 0, guess);
  }
  double getTolerance();
  double getFunctionValue();
  double getInitialCandidate();
  int getMaxIterations();
  int getIterations();
  Map<String, ?> getStats();
}
