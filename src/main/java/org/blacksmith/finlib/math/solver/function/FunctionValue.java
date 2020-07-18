package org.blacksmith.finlib.math.solver.function;

import java.text.MessageFormat;

import org.blacksmith.commons.arg.ArgChecker;

public class FunctionValue {
  private final double argument;
  private final double value;
  private final double[] derivatives;

  public FunctionValue(double argument, double value, double...derivatives) {
    this.argument = argument;
    this.value = value;
    this.derivatives = derivatives;
  }

  public double getValue() {
    return this.value;
  }

  public double getPartialDerivative(int derivative) {
    ArgChecker.isTrue(derivative>0 && derivative<=derivatives.length,()-> MessageFormat.format("Derivative number must be in range 1-{0}",derivatives.length));
    return derivatives[derivative-1];
  }

  public double getArgument() {
    return argument;
  }
}
