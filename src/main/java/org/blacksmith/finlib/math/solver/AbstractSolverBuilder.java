package org.blacksmith.finlib.math.solver;

public abstract class AbstractSolverBuilder<F extends SolverFunction,S extends Solver<F>> implements SolverBuilder<F,S> {
  public static final double TOLERANCE = 0.000_000_1;
  public static final long ITERATIONS = 10_000;
  protected double tolerance = TOLERANCE;
  protected long iterations = ITERATIONS;
  protected java.util.function.Function<Double, Double> argAligner = Double::doubleValue;
  protected boolean breakIfTheSameCandidate=false;

  public AbstractSolverBuilder() {
  }

  @Override
  public AbstractSolverBuilder<F,S> tolerance(double tolerance) {
    this.tolerance = tolerance;
    return this;
  }

  @Override
  public AbstractSolverBuilder<F,S> iterations(long iterations) {
    this.iterations = iterations;
    return this;
  }

  @Override
  public AbstractSolverBuilder<F,S> argAligner(java.util.function.Function<Double,Double> argAligner) {
    this.argAligner = argAligner;
    return this;
  }

  @Override
  public AbstractSolverBuilder<F,S> breakIfTheSameCandidate(boolean breakIfTheSameCandidate) {
    this.breakIfTheSameCandidate = breakIfTheSameCandidate;
    return this;
  }
}
