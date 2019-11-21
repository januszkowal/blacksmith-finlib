package org.blacksmith.finlib.math.solver;

import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AlgBiSectionTest {

  @Test
  public void sqrt2a() throws Exception {
    Solver nr = BiSectionAlgorithm.builder()
        .withMinArg(0)
        .withMaxArg(30)
        .withFunction(new Function() {
          @Override public double presentValue(double x) {
            return x*x;
          }
          @Override public double derivative(double x) {
            return 2*x;
          }
        })
        .build();
    assertEquals(2, nr.inverse(4, 4), TOLERANCE);
    assertEquals(3, nr.inverse(9, -9), TOLERANCE);
    assertEquals(25, nr.inverse(625, 625), TOLERANCE);
  }

  @Test
  public void sqrt2ab() throws Exception {
    Solver nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(new Function() {
          @Override public double presentValue(double x) {
            return x*x;
          }
          @Override public double derivative(double x) {
            return 2*x;
          }
        })
        .build();
    assertEquals(-2, nr.inverse(4, 4), TOLERANCE);
    assertEquals(-3, nr.inverse(9, -9), TOLERANCE);
    assertEquals(-25, nr.inverse(625, 625), TOLERANCE);
  }

  @Test
  public void cubeRoot() throws Exception {
    Solver nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(new Function() {
          @Override public double presentValue(double x) {
            return x*x*x;
          }
          @Override public double derivative(double x) {
            return 3*x*x;
          }
        })
        .build();
    assertEquals(2, nr.inverse(8, 8), TOLERANCE);
    assertEquals(-3, nr.inverse(-27, 27), TOLERANCE);
    assertEquals(25, nr.inverse(15_625, 15_625), TOLERANCE);
  }

  @Test
  public void quadratic_a() throws Exception {
    Solver nr = BiSectionAlgorithm.builder()
        .withMinArg(-30)
        .withMaxArg(30)
        .withFunction(new Function() {
          @Override public double presentValue(double x) {
            return (x-4)*(x+3);
          }
          @Override public double derivative(double x) {
            return 2*x-1;
          }
        })
        .build();
    assertEquals(-3, nr.findRoot(10), TOLERANCE);
    assertEquals(-3, nr.findRoot(-10), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(-3, nr.findRoot(.51), TOLERANCE);
    assertEquals(-3, nr.findRoot(.49), TOLERANCE);
  }

  @Test
  public void quadratic_b() throws Exception {
    Solver nr = BiSectionAlgorithm.builder()
        .withMinArg(0)
        .withMaxArg(30)
        .withFunction(new Function() {
          @Override public double presentValue(double x) {
            return (x-4)*(x+3);
          }
          @Override public double derivative(double x) {
            return 2*x-1;
          }
        })
        .build();
    assertEquals(4, nr.findRoot(10), TOLERANCE);
    assertEquals(4, nr.findRoot(-10), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(.51), TOLERANCE);
    assertEquals(4, nr.findRoot(.49), TOLERANCE);
  }
}
