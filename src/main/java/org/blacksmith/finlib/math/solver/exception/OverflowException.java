package org.blacksmith.finlib.math.solver.exception;

import java.util.Map;
import org.blacksmith.finlib.math.solver.Solver;

/**
 * Indicates that the algorithm failed to converge due to one of the values
 * (either the candidate value, the function value or derivative value) being
 * an invalid double (NaN, Infinity or -Infinity) or other condition leading to
 * an overflow.
 */
public class OverflowException extends ArithmeticException {

  private final Map<String,?> stats;

  public OverflowException(String message, Map<String,?> stats) {
    super(message);
    this.stats = stats;
  }

  public Map<String,?> getStats() {
    return stats;
  }

  @Override
  public String toString() {
    return super.toString() + ": " + stats;
  }

}
