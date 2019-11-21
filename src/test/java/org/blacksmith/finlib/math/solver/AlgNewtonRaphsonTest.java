package org.blacksmith.finlib.math.solver;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AlgNewtonRaphsonTest {

  @Test
  public void sqrt() throws Exception {
    Solver nr = NewtonRaphsonAlgorithm.builder()
        .withFunction(new Function() {
          @Override public double functionValue(double x) {
            return x*x;
          }
          @Override public double derivativeValue(double x) {
            return 2*x;
          }
        })
        .build();
    assertEquals(2, nr.inverse(4, 4.0), TOLERANCE);
    assertEquals(-3, nr.inverse(9, -9.0), TOLERANCE);
    assertEquals(25, nr.inverse(625, 625.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() throws Exception {
    Solver nr = NewtonRaphsonAlgorithm.builder()
        .withFunction(new Function() {
          @Override public double functionValue(double x) {
            return x*x*x;
          }
          @Override public double derivativeValue(double x) {
            return 3*x*x;
          }
        })
        .build();
    assertEquals(2, nr.inverse(8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(-27, 27.0), TOLERANCE);
    assertEquals(25, nr.inverse(15_625, 15_625.0), TOLERANCE);
  }

  @Test
  public void quadratic() throws Exception {
    Solver nr = NewtonRaphsonAlgorithm.builder()
        .withFunction(new Function() {
          @Override public double functionValue(double x) {
            return (x-4)*(x+3);
          }
          @Override public double derivativeValue(double x) {
            return 2*x-1;
          }
        })
        .build();
    assertEquals(4, nr.findRoot(10.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(-10.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(.51), TOLERANCE);
    assertEquals(-3, nr.findRoot(.49), TOLERANCE);
  }

  @Test
  public void failToConverge() throws Exception {
    Assertions.assertThrows(ZeroValuedDerivativeException.class,()->{
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return (x-4)*(x+3);
            }
            @Override public double derivativeValue(double x) {
              return 2*x-1;
            }
          })
          .build();
      // Inflection point when derivative is zero => x = 1/2
      nr.findRoot(.5);
      fail("Expected zero-valued derivative");
    });
  }

  @Test
  public void failToConverge_verifyDetails() throws Exception {
    try {
      // Use nonsense functions designed to cause a zero derivative
      // after one iteration
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return 2;
            }
            @Override public double derivativeValue(double x) {
              return x > 0 ? .25 : 0;
            }
          })
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (ZeroValuedDerivativeException zvde) {
      assertEquals(3, zvde.getInitialGuess(), TOLERANCE);
      assertEquals(2, zvde.getIteration());
      assertEquals(-5, zvde.getArgument(), TOLERANCE);
      assertEquals(2, zvde.getFunctionValue(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_iterations() throws Exception {
    Assertions.assertThrows(NonconvergenceException.class,()->{
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return 2 * Math.signum(x);
            }
            @Override public double derivativeValue(double x) {
              return 1;
            }
          })
          .build();
      nr.findRoot(1.0);
      fail("Expected non-convergence");
    });
  }

  @Test
  public void failToConverge_iterations_verifyDetails() throws Exception {
    try {
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return 2 * Math.signum(x);
            }
            @Override public double derivativeValue(double x) {
              return 1;
            }
          })
          .build();
      nr.findRoot(1.0);
      fail("Expected non-convergence");
    } catch (NonconvergenceException ne) {
      assertEquals(1, ne.getInitialGuess(), TOLERANCE);
      assertEquals(10_000L, ne.getIterations());
    }
  }

  @Test
  public void failToConverge_badCandidate_verifyDetails() throws Exception {
    try {
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return Double.MAX_VALUE;
            }
            @Override public double derivativeValue(double x) {
              return Double.MIN_NORMAL;
            }
          })
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      System.out.println(ne);
      assertEquals(3, ne.getInitialGuess(), TOLERANCE);
      assertEquals(1, ne.getIteration());
      assertEquals(Double.NEGATIVE_INFINITY, ne.getArgument(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_nanFunctionValue_verifyDetails() throws Exception {
    try {
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double x) {
              return Double.NaN;
            }
            @Override public double derivativeValue(double x) {
              return 1;
            }
          })
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      assertEquals(3, ne.getInitialGuess(), TOLERANCE);
      assertEquals(1, ne.getIteration());
      assertEquals(3, ne.getArgument(), TOLERANCE);
      assertTrue(Double.isNaN(ne.getFunctionValue()));
      assertNull(ne.getDerivativeValue());
    }
  }

  @Test
  public void failToConverge_nanDerivative_verifyDetails() throws Exception {
    try {
      Solver nr = NewtonRaphsonAlgorithm.builder()
          .withFunction(new Function() {
            @Override public double functionValue(double arg) {
              return 2;
            }
            @Override public double derivativeValue(double arg) {
              return Double.NaN;
            }
          })
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      assertEquals(3, ne.getInitialGuess(), TOLERANCE);
      assertEquals(1, ne.getIteration());
      assertEquals(3, ne.getArgument(), TOLERANCE);
      assertEquals(2, ne.getFunctionValue(), TOLERANCE);
      assertTrue(Double.isNaN(ne.getDerivativeValue()));
    }
  }


  @Test
  public void tolerance() throws Exception {
    final double tolerance = TOLERANCE/1000;
    Solver nr = NewtonRaphsonAlgorithm.builder()
        .withFunction(new Function() {
          @Override public double functionValue(double x) {
            return x*x;
          }
          @Override public double derivativeValue(double x) {
            return 2*x;
          }
        })
        .withTolerance(tolerance)
        .build();
    assertEquals(4, nr.inverse(16, 16.0), tolerance);
    assertEquals(15, nr.inverse(225, 225.0), tolerance);
    assertEquals(1.414_213_562_3, nr.inverse(2, 2.0), tolerance);
  }

}
