package org.blacksmith.finlib.math.solver.function;

public interface SolverFunctionDerivative extends SolverFunction {
  default FunctionValue computeValueAndDerivatives(double arg) {
    String ccc = this.getClass().toString();
    double[] derivatives = new double[numberOfDerivatives()];
    for (int i = 0; i < numberOfDerivatives(); i++) {
      derivatives[i] = computeDerivative(i + 1, arg);
    }
    return new FunctionValue(arg, computeValue(arg), derivatives);
  }

  default int numberOfDerivatives() {
    return 0;
  }

  default double computeDerivative(int derivativeNumber, double arg) {
    return 0d;
  }
}
