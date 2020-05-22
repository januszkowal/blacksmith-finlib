package org.blacksmith.finlib.math.solver;

import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.blacksmith.finlib.math.solver.exception.NonconvergenceException;
import org.blacksmith.finlib.math.solver.exception.OverflowException;
import org.blacksmith.finlib.math.solver.exception.ZeroValuedDerivativeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AlgNewtonRaphsonTest {

  Function1stDeriv funSqrt = new Function1stDeriv() {
    @Override
    public double value(double x) {
      return x * x;
    }

    @Override
    public double derivative(double x) {
      return 2 * x;
    }
  };

  Function1stDeriv funCubeRoot = new Function1stDeriv() {
    @Override
    public double value(double x) {
      return x * x * x;
    }

    @Override
    public double derivative(double x) {
      return 3 * x * x;
    }
  };

  Function1stDeriv funQuadratic = new Function1stDeriv() {
    @Override
    public double value(double x) {
      return (x - 4) * (x + 3);
    }

    @Override
    public double derivative(double x) {
      return 2 * x - 1;
    }
  };

  @Test
  public void sqrt() throws Exception {
    Solver nr = NewtonRaphsonSolverBuilder.builder()
        .withFunction(funSqrt)
        .build();
    assertEquals(2, nr.inverse(4, 4.0), TOLERANCE);
    assertEquals(-3, nr.inverse(9, -9.0), TOLERANCE);
    assertEquals(25, nr.inverse(625, 625.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() throws Exception {
    Solver nr = NewtonRaphsonSolverBuilder.builder()
        .withFunction(funCubeRoot)
        .build();
    assertEquals(2, nr.inverse(8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(-27, 27.0), TOLERANCE);
    assertEquals(25, nr.inverse(15_625, 15_625.0), TOLERANCE);
  }

  @Test
  public void quadratic() throws Exception {
    Solver nr = NewtonRaphsonSolverBuilder.builder()
        .withFunction(funQuadratic)
        .build();
    assertEquals(4, nr.findRoot(10.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(-10.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(.51), TOLERANCE);
    assertEquals(-3, nr.findRoot(.49), TOLERANCE);
  }

  @Test
  public void failToConverge() throws Exception {
    var function = new Function1stDeriv() {
      @Override
      public double value(double x) {
        return (x - 4) * (x + 3);
      }

      @Override
      public double derivative(double x) {
        return 2 * x - 1;
      }
    };
    Assertions.assertThrows(ZeroValuedDerivativeException.class, () -> {
      Solver nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      // Inflection point when derivative is zero => x = 1/2
      nr.findRoot(.5);
      fail("Expected zero-valued derivative");
    });
  }

  @Test
  public void failToConverge_verifyDetails() throws Exception {
    NewtonRaphsonSolver nr = null;
    var function = new Function1stDeriv() {
      @Override
      public double value(double x) {
        return 2;
      }

      @Override
      public double derivative(double x) {
        return x > 0 ? .25 : 0;
      }
    };
    try {
      // Use nonsense functions designed to cause a zero derivative
      // after one iteration
      nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (ZeroValuedDerivativeException zvde) {
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(2, nr.getIterations());
      assertEquals(-5, nr.getCandidate(), TOLERANCE);
      assertEquals(2, nr.getFunctionValue(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_iterations() throws Exception {
    var function = new Function1stDeriv() {
      @Override
      public double value(double x) {
        return 2 * Math.signum(x);
      }

      @Override
      public double derivative(double x) {
        return 1;
      }
    };
    Assertions.assertThrows(NonconvergenceException.class, () -> {
      Solver nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      nr.findRoot(1.0);
      fail("Expected non-convergence");
    });
  }

  @Test
  public void failToConverge_iterations_verifyDetails() throws Exception {
    var function = new Function1stDeriv() {
      @Override
      public double value(double x) {
        return 2 * Math.signum(x);
      }
      @Override
      public double derivative(double x) {
        return 1;
      }
    };
    try {
      Solver nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
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
    var function = new Function1stDeriv() {
      @Override
      public double value(double arg) {
        return Double.MAX_VALUE;
      }

      @Override
      public double derivative(double arg) {
        return Double.MIN_NORMAL;
      }
    };
    Solver nr = null;
    try {
      nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      System.out.println(ne);
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(1, nr.getIterations());
      assertEquals(Double.NEGATIVE_INFINITY, nr.getCandidate(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_nanFunctionValue_verifyDetails() throws Exception {
    var function = new Function1stDeriv() {
      @Override
      public double value(double arg) {
        return Double.NaN;
      }

      @Override
      public double derivative(double arg) {
        return 1;
      }
    };
    NewtonRaphsonSolver nr = null;
    try {
          nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(1, nr.getIterations());
      assertEquals(3, nr.getCandidate(), TOLERANCE);
      assertTrue(Double.isNaN(nr.getFunctionValue()));
      //assertNull(nr.getDerivativeValue());
    }
  }

  @Test
  public void failToConverge_nanDerivative_verifyDetails() throws Exception {
    var function = new Function1stDeriv() {
      @Override
      public double value(double arg) {
        return 2;
      }

      @Override
      public double derivative(double arg) {
        return Double.NaN;
      }
    };
    NewtonRaphsonSolver nr = null;
    try {

      nr = NewtonRaphsonSolverBuilder.builder()
          .withFunction(function)
          .build();
      nr.findRoot(3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(1, nr.getIterations());
      assertEquals(3, nr.getCandidate(), TOLERANCE);
      assertEquals(2, nr.getFunctionValue(), TOLERANCE);
      assertTrue(Double.isNaN(nr.getDerivativeValue()));
    }
  }


  @Test
  public void tolerance() throws Exception {
    final double tolerance = TOLERANCE / 1000;
    var function = new Function1stDeriv() {
      @Override
      public double value(double x) {
        return x*x;
      }

      @Override
      public double derivative(double x) {
        return 2*x;
      }
    };
    Solver nr = NewtonRaphsonSolverBuilder.builder()
        .withFunction(function)
        .withTolerance(tolerance)
        .build();
    assertEquals(4, nr.inverse(16, 16.0), tolerance);
    assertEquals(15, nr.inverse(225, 225.0), tolerance);
    assertEquals(1.414_213_562_3, nr.inverse(2, 2.0), tolerance);
  }

}
