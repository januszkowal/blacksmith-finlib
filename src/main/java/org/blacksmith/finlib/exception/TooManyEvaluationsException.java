package org.blacksmith.finlib.exception;

public class TooManyEvaluationsException extends IllegalStateException {
  private static final long serialVersionUID = 2735728594056232663L;
  private final Number max;

  public TooManyEvaluationsException(Number max) {
    this("Too many evaluations", max);
  }

  public TooManyEvaluationsException(String message, Number max) {
    super(message);
    this.max = max;
  }
}
