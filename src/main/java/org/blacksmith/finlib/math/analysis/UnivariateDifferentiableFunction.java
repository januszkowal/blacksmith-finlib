package org.blacksmith.finlib.math.analysis;


public interface UnivariateDifferentiableFunction extends UnivariateFunction {
  default DerivativeValue computeValueAndDerivative(double arg) {
    double[] derivatives = new double[numberOfDerivatives()];
    for (int i = 0; i < numberOfDerivatives(); i++) {
      derivatives[i] = computeDerivative(i + 1, arg);
    }
    return new DerivativeValue(arg, value(arg), derivatives);
  }

  default int numberOfDerivatives() {
    return 0;
  }

  default double computeDerivative(int derivativeNumber, double arg) {
    return 0d;
  }
}
