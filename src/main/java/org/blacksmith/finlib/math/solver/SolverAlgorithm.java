package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.function.SolverFunction;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;

public enum SolverAlgorithm {
  BI_SECTION(SolverFunction.class),
  NEWTON_RAPHSON(SolverFunctionDerivative.class);

  private final Class<? extends SolverFunction> solverFunctionClass;

  SolverAlgorithm(Class<? extends SolverFunction> solverFunctionClass) {
    this.solverFunctionClass = solverFunctionClass;
  }

  public Class<? extends SolverFunction> getSolverFunctionClass() {
    return this.solverFunctionClass;
  }
}
