package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.function.SolverFunctionDerivative;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

public class XirrBuilderTest {

  @Test
  public void withTransactions_1_year_growth() {
    //given
    var cashflows = List.of(
        Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 1100));
    // computes the xirr on 1 year growth of 10%
    final double xirr = XirrCalculatorBuilder.builder()
        .withSolverFunctionDerivative(NewtonRaphsonSolverBuilder.builder().build())
        .build().xirr(cashflows);
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_decline() {
    var cashflows = List.of(
        Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 900));
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = XirrCalculatorBuilder.builder()
        .withSolverFunctionDerivative(NewtonRaphsonSolverBuilder.builder().build())
        .build().xirr(cashflows);
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void withNewtonRaphsonBuilder() {
    final double expected = 1;
    var cashflows = List.of(
        Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 1000));

    Solver<SolverFunctionDerivative> solver = Mockito.mock(Solver.class);
    Mockito.when(solver.findRoot(any(SolverFunctionDerivative.class), anyDouble())).thenReturn(expected);

    final double xirr = XirrCalculatorBuilder.builder()
        .withSolverFunctionDerivative(solver)
        .build().xirr(cashflows);

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  @Test
  public void withGuess() {
    final double expected = 1;
    final double guess = 3;
    var cashflows = List.of(
        Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 1000));

    Solver<SolverFunctionDerivative> solver = Mockito.mock(Solver.class);
    Mockito.when(solver.findRoot(any(SolverFunctionDerivative.class), eq(guess))).thenReturn(expected);

    final double xirr = XirrCalculatorBuilder.builder()
        .withGuess(guess)
        .withSolverFunctionDerivative(solver)
        .build().xirr(cashflows);
    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }
}
