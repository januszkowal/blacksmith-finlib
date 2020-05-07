package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;
import java.util.Arrays;
import org.blacksmith.finlib.math.solver.Solver;
import org.blacksmith.finlib.math.solver.SolverBuilder;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.Xirr;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrBuilderTest {

  @Test
  public void withTransactions_1_year_no_growth() {
    // computes the xirr on 1 year growth of 0%
    final double xirr = Xirr.builder()
        .withCashflows(Arrays.asList(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),1000)
        )).build().xirr();
    assertEquals(0, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = Xirr.builder()
        .withCashflows(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),1100)
        ).build().xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = Xirr.builder()
        .withCashflows(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),900)
        ).build().xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void withNewtonRaphsonBuilder() throws Exception {
    final double expected = 1;

    final SolverBuilder builder = setUpNewtonRaphsonBuilder();
    System.out.println("builder build:" + builder.build());
    Mockito.when(builder.build().findRoot(ArgumentMatchers.anyDouble())).thenReturn(expected);

    final double xirr = Xirr.builder()
        .withSolverBuilder(builder)
        .withCashflows(
            Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
            Cashflow.of(LocalDate.parse("2011-01-01"),1000)
        ).build().xirr();

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  @Test
  public void withGuess() {
    final double expected = 1;
    final double guess = 3;

    final SolverBuilder builder = setUpNewtonRaphsonBuilder();
    Mockito.when(builder.build().findRoot(guess)).thenReturn(expected);

    final double xirr = Xirr.builder()
        .withGuess(guess)
        .withSolverBuilder(builder)
        .withCashflows(
            Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
            Cashflow.of(LocalDate.parse("2011-01-01"), 1000)
        ).build().xirr();

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  private SolverBuilder setUpNewtonRaphsonBuilder()
  {
    final SolverBuilder builder = Mockito.mock(SolverBuilder.class);
    final Solver solver = Mockito.mock(Solver.class);
    Mockito.when(builder.withFunction(ArgumentMatchers.any())).thenReturn(builder);
    Mockito.when(builder.build()).thenReturn(solver);
    return builder;
  }

}