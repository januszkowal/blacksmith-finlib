package org.blacksmith.finlib.math.solver.function;

public interface SolverFunctionDerivative extends SolverFunction {
  default FunctionValue getFunctionValue(double arg) {
    return new FunctionValue(arg,getValue(arg),getDerivative(arg));
  }
  default double getDerivative(double arg) {
    return 0d;
  }
}
