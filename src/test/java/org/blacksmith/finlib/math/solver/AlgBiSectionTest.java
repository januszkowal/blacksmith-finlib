package org.blacksmith.finlib.math.solver;

import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgBiSectionTest {

  @Test
  public void sqrt2a() {
    Solver<?> nr = BiSectionAlgorithm.builder()
        .withMinArg(0)
        .withMaxArg(30)
        .withFunction(x->x*x)
        .build();
    assertEquals(2, nr.inverse(4, 0.0), TOLERANCE);
    assertEquals(3, nr.inverse(9, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(625, 0.0), TOLERANCE);
  }

  @Test
  public void sqrt2ab() {
    Solver<?> nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(x->x*x)
        .build();
    assertEquals(-2, nr.inverse(4, 0.0), TOLERANCE);
    assertEquals(-3, nr.inverse(9, 0.0), TOLERANCE);
    assertEquals(-25, nr.inverse(625, 0.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() {
    Solver<?> nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(x->x*x*x)
        .build();
    assertEquals(2, nr.inverse(8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(-27, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(15_625, 0.0), TOLERANCE);
  }

  @Test
  public void quadratic_a() {
    Solver<?> nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(x->(x-4)*(x+3))
        .build();
    assertEquals(-3, nr.findRoot(0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(0.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(-3, nr.findRoot(0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(0.0), TOLERANCE);
  }

  @Test
  public void quadratic_b() {
    Solver<?> nr = BiSectionAlgorithm.builder()
        .withMinArg(0)
        .withMaxArg(30)
        .withFunction(x->(x-4)*(x+3))
        .build();
    assertEquals(4, nr.findRoot(0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(0.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(0.0), TOLERANCE);
  }
}
