package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public enum SolverAlgorithm {
  BI_SECTION(UnivariateFunction.class),
  NEWTON_RAPHSON(UnivariateDifferentiableFunction.class);

  private final Class<? extends UnivariateFunction> solverFunctionClass;

  SolverAlgorithm(Class<? extends UnivariateFunction> solverFunctionClass) {
    this.solverFunctionClass = solverFunctionClass;
  }

  public Class<? extends UnivariateFunction> getSolverFunctionClass() {
    return this.solverFunctionClass;
  }
}
