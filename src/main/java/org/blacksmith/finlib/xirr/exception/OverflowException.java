package org.blacksmith.finlib.xirr.exception;

import org.blacksmith.finlib.xirr.solver.Solver;

/**
 * Indicates that the algorithm failed to converge due to one of the values
 * (either the candidate value, the function value or derivative value) being
 * an invalid double (NaN, Infinity or -Infinity) or other condition leading to
 * an overflow.
 */
public class OverflowException extends ArithmeticException {

  private final Solver state;

  public OverflowException(String message, Solver state) {
    super(message);
    this.state = state;
  }

  /**
   * Get the initial guess used by the algorithm.
   * @return the initial guess
   */
  public double getInitialGuess() {
    return state.getInitialGuess();
  }

  /**
   * Get the number of iterations passed when encountering the overflow.
   * @return the number of iterations passed when encountering the overflow
   * condition
   */
  public long getIteration() {
    return state.getIteration();
  }

  /**
   * Get the candidate value when the overflow condition occurred.
   * @return the candidate value when the overflow condition occurred
   */
  public double getArgument() {
    return state.getArgument();
  }

  /**
   * Get the function value when the overflow condition occurred.
   * @return the function value when the overflow condition occurred
   */
  public double getFunctionValue() {
    return state.getFunctionValue();
  }

  /**
   * Get the derivative value when the overflow condition occurred.  A null
   * value indicates the derivative was not yet calculated.
   * @return the derivative value when the overflow condition occurred
   */
  public Double getDerivativeValue() {
    return state.getDerivativeValue();
  }

  @Override
  public String toString() {
    return super.toString() + ": " + state;
  }

}
