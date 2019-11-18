package org.blacksmith.finlib.xirr;

import java.time.LocalDate;
import java.util.Arrays;
import org.blacksmith.finlib.xirr.solver.NewtonMethod;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.blacksmith.finlib.xirr.solver.NewtonMethod.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrBuilderTest {

  @Test
  public void withTransactions_1_year_no_growth() {
    // computes the xirr on 1 year growth of 0%
    final double xirr = Xirr.builder()
        .withCashflows(Arrays.asList(
            new Cashflow(-1000,LocalDate.parse("2010-01-01")),
            new Cashflow( 1000,LocalDate.parse("2011-01-01"))
        )).build().xirr();
    assertEquals(0, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = Xirr.builder()
        .withCashflows(
            new Cashflow(-1000,LocalDate.parse("2010-01-01")),
            new Cashflow( 1100,LocalDate.parse("2011-01-01"))
        ).build().xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void withTransactions_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = Xirr.builder()
        .withCashflows(
            new Cashflow(-1000,LocalDate.parse("2010-01-01")),
            new Cashflow(  900,LocalDate.parse("2011-01-01"))
        ).build().xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void withNewtonRaphsonBuilder() throws Exception {
    final double expected = 1;

    final NewtonMethod.Builder builder = setUpNewtonRaphsonBuilder();
    Mockito.when(builder.build().findRoot(ArgumentMatchers.anyDouble())).thenReturn(expected);

    final double xirr = Xirr.builder()
        .withSolverBuilder(builder)
        .withCashflows(
            new Cashflow(-1000,LocalDate.parse("2010-01-01")),
            new Cashflow( 1000,LocalDate.parse("2011-01-01"))
        ).build().xirr();

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  @Test
  public void withGuess() {
    final double expected = 1;
    final double guess = 3;

    final NewtonMethod.Builder builder = setUpNewtonRaphsonBuilder();
    Mockito.when(builder.build().findRoot(guess)).thenReturn(expected);

    final double xirr = Xirr.builder()
        .withGuess(guess)
        .withSolverBuilder(builder)
        .withCashflows(
            new Cashflow(-1000, LocalDate.parse("2010-01-01")),
            new Cashflow( 1000, LocalDate.parse("2011-01-01"))
        ).build().xirr();

    // Correct answer is 0, but we are ensuring that Xirr is using the
    // builder we supplied
    assertEquals(expected, xirr, 0);
  }

  private NewtonMethod.Builder setUpNewtonRaphsonBuilder()
  {
    final NewtonMethod.Builder builder = Mockito.mock(NewtonMethod.Builder.class);
    Mockito.when(builder.withFunction(ArgumentMatchers.any())).thenReturn(builder);
    return builder;
  }

}
