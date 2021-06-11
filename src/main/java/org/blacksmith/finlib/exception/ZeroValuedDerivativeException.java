package org.blacksmith.finlib.exception;

import java.util.Map;

/**
 * Indicates that the numerical method employed encountered a zero-valued
 * derivative, terminating the algorithm unsuccessfully.
 * <p>
 * The state of the algorithm is available via the getters, to allow the caller
 * to adjust the guess and try again.
 *
 * @author ray
 */
public class ZeroValuedDerivativeException extends IllegalStateException {

  public ZeroValuedDerivativeException() {
    super("Solver failed due to zero-valued derivative.");
  }
}
