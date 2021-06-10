package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;

public abstract class AbstractUnivariateDifferentiableSolver
    extends AbstractSolver<UnivariateDifferentiableFunction>
    implements UnivariateDifferentiableSolver {

  public AbstractUnivariateDifferentiableSolver(int maxIterations, double min, double max, double tolerance) {
    super(maxIterations, min, max, tolerance);
  }
}
