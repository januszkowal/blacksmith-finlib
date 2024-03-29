package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.analysis.UnivariateDifferentiableFunction;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.junit.jupiter.api.Test;

import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgBiSectionTest {

  @Test
  public void sqrt2a() {
    UnivariateDifferentiableFunction f = x -> x * x;
    Solver<UnivariateFunction> nr = BiSectionSolverBuilder.builder()
        .minArg(0)
        .maxArg(30)
        .build();
    assertEquals(2, nr.inverse(f, 4, 0.0), TOLERANCE);
    assertEquals(3, nr.inverse(f, 9, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(f, 625, 0.0), TOLERANCE);
  }

  @Test
  public void sqrt2ab() {
    UnivariateDifferentiableFunction f = x -> x * x;
    Solver<UnivariateFunction> nr = BiSectionSolverBuilder.builder()
        .minArg(-30)
        .maxArg(30)
        .maxIterations(100)
        .build();
    assertEquals(-2, nr.inverse(f, 4, 0.0), TOLERANCE);
    assertEquals(-3, nr.inverse(f, 9, 0.0), TOLERANCE);
    assertEquals(-25, nr.inverse(f, 625, 0.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() {
    UnivariateDifferentiableFunction f = x -> x * x * x;
    Solver<UnivariateFunction> nr = BiSectionSolverBuilder.builder()
        .minArg(-30)
        .maxArg(30)
        .build();
    assertEquals(2, nr.inverse(f, 8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(f, -27, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(f, 15_625, 0.0), TOLERANCE);
  }

  @Test
  public void quadratic_a() {
    UnivariateDifferentiableFunction f = x -> (x - 4) * (x + 3);
    Solver<UnivariateFunction> nr = BiSectionSolverBuilder.builder()
        .minArg(-30)
        .maxArg(30)
        .build();
    assertEquals(-3, nr.findRoot(f, 0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(f, 0.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(-3, nr.findRoot(f, 0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(f, 0.0), TOLERANCE);
  }

  @Test
  public void quadratic_b() {
    UnivariateDifferentiableFunction f = x -> (x - 4) * (x + 3);
    Solver<UnivariateFunction> nr = BiSectionSolverBuilder.builder()
        .minArg(0)
        .maxArg(30)
        .build();
    assertEquals(4, nr.findRoot(f, 0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(f, 0.0), TOLERANCE);
    //     Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(f, 0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(f, 0.0), TOLERANCE);
  }
}
