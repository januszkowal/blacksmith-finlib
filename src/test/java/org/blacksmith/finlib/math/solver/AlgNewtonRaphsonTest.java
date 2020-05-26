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

  final Function1stDerivative funSqrt = new Function1stDerivative() {
    @Override
    public double value(double x) {
      return x * x;
    }

    @Override
    public double derivative(double x) {
      return 2 * x;
    }
  };

  final Function1stDerivative funCubeRoot = new Function1stDerivative() {
    @Override
    public double value(double x) {
      return x * x * x;
    }

    @Override
    public double derivative(double x) {
      return 3 * x * x;
    }
  };

  final Function1stDerivative funQuadratic = new Function1stDerivative() {
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
  public void sqrt() {
    Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(funSqrt)
        .build();
    assertEquals(2, nr.inverse(funSqrt,4, 4.0), TOLERANCE);
    assertEquals(-3, nr.inverse(funSqrt,9, -9.0), TOLERANCE);
    assertEquals(25, nr.inverse(funSqrt,625, 625.0), TOLERANCE);
  }

  @Test
  public void cubeRoot() {
    Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(funCubeRoot)
        .build();
    assertEquals(2, nr.inverse(funCubeRoot,8, 8.0), TOLERANCE);
    assertEquals(-3, nr.inverse(funCubeRoot,-27, 27.0), TOLERANCE);
    assertEquals(25, nr.inverse(funCubeRoot,15_625, 15_625.0), TOLERANCE);
  }

  @Test
  public void quadratic() {
    Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(funQuadratic)
        .build();
    assertEquals(4, nr.findRoot(funQuadratic,10.0), TOLERANCE);
    assertEquals(-3, nr.findRoot(funQuadratic,-10.0), TOLERANCE);
    // Inflection point when derivative is zero => x = 1/2
    assertEquals(4, nr.findRoot(funQuadratic,.51), TOLERANCE);
    assertEquals(-3, nr.findRoot(funQuadratic,.49), TOLERANCE);
  }

  @Test
  public void failToConverge() {
    var function = new Function1stDerivative() {
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
      Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
          //.withFunction(function)
          .build();
      // Inflection point when derivative is zero => x = 1/2
      nr.findRoot(function,.5);
      fail("Expected zero-valued derivative");
    });
  }

  @Test
  public void failToConverge_verifyDetails() {
    // Use nonsense functions designed to cause a zero derivative
    // after one iteration
    var function = new Function1stDerivative() {
      @Override
      public double value(double x) {
        return 2;
      }

      @Override
      public double derivative(double x) {
        return x > 0 ? .25 : 0;
      }
    };
    NewtonRaphsonSolver nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(function)
        .build();
    try {
      nr.findRoot(function,3.0);
      fail("Expected non-convergence");
    } catch (ZeroValuedDerivativeException e) {
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(2, nr.getIterations());
      assertEquals(-5, nr.getCandidate(), TOLERANCE);
      assertEquals(2, nr.getFunctionValue(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_iterations() {
    var function = new Function1stDerivative() {
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
      Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
          //.withFunction(function)
          .build();
      nr.findRoot(function,1.0);
      fail("Expected non-convergence");
    });
  }

  @Test
  public void failToConverge_iterations_verifyDetails() {
    var function = new Function1stDerivative() {
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
      Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
          //.withFunction(function)
          .build();
      nr.findRoot(function,1.0);
      fail("Expected non-convergence");
    } catch (NonconvergenceException ne) {
      assertEquals(1, ne.getInitialGuess(), TOLERANCE);
      assertEquals(10_000L, ne.getIterations());
    }
  }

  @Test
  public void failToConverge_badCandidate_verifyDetails() {
    var function = new Function1stDerivative() {
      @Override
      public double value(double arg) {
        return Double.MAX_VALUE;
      }

      @Override
      public double derivative(double arg) {
        return Double.MIN_NORMAL;
      }
    };
    Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(function)
        .build();
    try {
      nr.findRoot(function,3.0);
      fail("Expected non-convergence");
    } catch (OverflowException ne) {
      ne.printStackTrace();
      assertEquals(3, nr.getInitialGuess(), TOLERANCE);
      assertEquals(1, nr.getIterations());
      assertEquals(Double.NEGATIVE_INFINITY, nr.getCandidate(), TOLERANCE);
    }
  }

  @Test
  public void failToConverge_nanFunctionValue_verifyDetails() {
    var function = new Function1stDerivative() {
      @Override
      public double value(double arg) {
        return Double.NaN;
      }

      @Override
      public double derivative(double arg) {
        return 1;
      }
    };
    NewtonRaphsonSolver nr =  NewtonRaphsonSolverBuilder.builder()
        //.withFunction(function)
        .build();
    try {
      nr.findRoot(function,3.0);
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
  public void failToConverge_nanDerivative_verifyDetails() {
    var function = new Function1stDerivative() {
      @Override
      public double value(double arg) {
        return 2;
      }

      @Override
      public double derivative(double arg) {
        return Double.NaN;
      }
    };
    NewtonRaphsonSolver nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(function)
        .build();
    try {
      nr.findRoot(function,3.0);
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
  public void tolerance() {
    final double tolerance = TOLERANCE / 1000;
    var function = new Function1stDerivative() {
      @Override
      public double value(double x) {
        return x*x;
      }

      @Override
      public double derivative(double x) {
        return 2*x;
      }
    };
    Solver<Function1stDerivative> nr = NewtonRaphsonSolverBuilder.builder()
        //.withFunction(function)
        .withTolerance(tolerance)
        .build();
    assertEquals(4, nr.inverse(function,16, 16.0), tolerance);
    assertEquals(15, nr.inverse(function,225, 225.0), tolerance);
    assertEquals(1.414_213_562_3, nr.inverse(function,2, 2.0), tolerance);
  }

}
