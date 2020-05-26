package org.blacksmith.finlib.math.solver;

import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlgBiSectionTest {

  @Test
  public void sqrt2a() {
    Function f = x->x*x;
    Solver<Function> nr = BiSectionSolverBuilder.builder()
        .withMinArg(0)
        .withMaxArg(30)
        //.withFunction(x->x*x)
        .build();
    assertEquals(2, nr.inverse(f,4, 0.0), TOLERANCE);
    assertEquals(3, nr.inverse(f,9, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(f,625, 0.0), TOLERANCE);
  }

  @Test
  public void sqrt2ab() {
    Function f = x->x*x;
    Solver<Function> nr = BiSectionSolverBuilder.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        //.withFunction(x->x*x)
        .build();
    assertEquals(-2, nr.inverse(f,4, 0.0), TOLERANCE);
    assertEquals(-3, nr.inverse(f,9, 0.0), TOLERANCE);
    assertEquals(-25, nr.inverse(f,625, 0.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() {
    Function f = x->x*x*x;
    Solver<Function> nr = BiSectionSolverBuilder.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        //.withFunction(x->x*x*x)
        .build();
    assertEquals(2, nr.inverse(f,8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(f,-27, 0.0), TOLERANCE);
    assertEquals(25, nr.inverse(f,15_625, 0.0), TOLERANCE);
  }

  @Test
  public void quadratic_a() {
    Function f = x->(x-4)*(x+3);
    Solver<Function> nr = BiSectionSolverBuilder.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        //.withFunction(x->(x-4)*(x+3))
        .build();
    assertEquals(-3, nr.findRoot(f,0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(f,0.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(-3, nr.findRoot(f,0.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(f,0.0), TOLERANCE);
  }

  @Test
  public void quadratic_b() {
    Function f = x->(x-4)*(x+3);
    Solver<Function> nr = BiSectionSolverBuilder.builder()
        .withMinArg(0)
        .withMaxArg(30)
        //.withFunction(x->(x-4)*(x+3))
        .build();
    assertEquals(4, nr.findRoot(f,0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(f,0.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(f,0.0), TOLERANCE);
    assertEquals(4, nr.findRoot(f,0.0), TOLERANCE);
  }
}
