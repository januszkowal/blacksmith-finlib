package org.blacksmith.finlib.exception;

import java.util.Collections;
import java.util.Map;

import org.apache.groovy.util.Maps;

/**
 * Indicates that the algorithm failed to converge due to one of the values
 * (either the candidate value, the function value or derivative value) being
 * an invalid double (NaN, Infinity or -Infinity) or other condition leading to
 * an overflow.
 */
public class OverflowException extends ArithmeticException {

  private final Map<String, ?> stats;

  public OverflowException(String message) {
    super(message);
    this.stats = Collections.emptyMap();
  }

  public OverflowException(String message, Map<String, ?> stats) {
    super(message);
    this.stats = stats;
  }

  public Map<String, ?> getStats() {
    return stats;
  }
}
