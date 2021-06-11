package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateFunction;

public abstract class AbstractUnivariateSolver
    extends AbstractSolver<UnivariateFunction>
    implements UnivariateSolver {
  public AbstractUnivariateSolver(int maxIterations, double min, double max, double tolerance) {
    super(maxIterations, min, max, tolerance);
  }
}
