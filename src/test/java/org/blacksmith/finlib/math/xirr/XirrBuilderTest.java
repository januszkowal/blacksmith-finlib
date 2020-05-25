package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;
import java.util.List;
import org.blacksmith.finlib.math.solver.Function1stDerivative;
import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.SolverBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrBuilderTest {

  @Test
  public void withTransactions_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = XirrCalculatorBuilder.<Function1stDerivative>builder()
        .withSolverBuilder(NewtonRaphsonSolverBuilder.builder())
        .withCashflows(List.of(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),1100))
        ).build().xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = XirrCalculatorBuilder.<Function1stDerivative>builder()
        .withSolverBuilder(NewtonRaphsonSolverBuilder.builder())
        .withCashflows(List.of(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),900))
        ).build().xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void withNewtonRaphsonBuilder() {
    final double expected = 1;

    final SolverBuilder<Function1stDerivative,Solver<Function1stDerivative>> builder = setUpNewtonRaphsonBuilder();
    System.out.println("builder build:" + builder.build());
    Mockito.when(builder.build().findRoot(ArgumentMatchers.anyDouble())).thenReturn(expected);

    final double xirr = XirrCalculatorBuilder.<Function1stDerivative>builder()
        .withSolverBuilder(builder)
        .withCashflows(List.of(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),1000))
        ).build().xirr();

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  @Test
  public void withGuess() {
    final double expected = 1;
    final double guess = 3;

    final SolverBuilder<Function1stDerivative,Solver<Function1stDerivative>> builder = setUpNewtonRaphsonBuilder();
    Mockito.when(builder.build().findRoot(guess)).thenReturn(expected);

    final double xirr = XirrCalculatorBuilder.<Function1stDerivative>builder()
        .withGuess(guess)
        .withSolverBuilder(builder)
        .withCashflows(List.of(
            Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
            Cashflow.of(LocalDate.parse("2011-01-01"), 1000))
        ).build().xirr();
    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  private SolverBuilder<Function1stDerivative,Solver<Function1stDerivative>> setUpNewtonRaphsonBuilder()
  {
    final SolverBuilder<Function1stDerivative,Solver<Function1stDerivative>> builder = Mockito.mock(SolverBuilder.class);
    final Solver<Function1stDerivative> solver = Mockito.mock(Solver.class);
    Mockito.when(builder.withFunction(ArgumentMatchers.any())).thenReturn(builder);
    Mockito.when(builder.build()).thenReturn(solver);
    return builder;
  }

}
