package org.blacksmith.finlib.math.xirr;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.blacksmith.finlib.math.solver.BiSectionSolverBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class XirrWrongInputTest {

  @Test
  public void xirr_no_transactions() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // throws exception when no transactions are passed
      new XirrCalculator(null).xirr(Collections.emptyList());
      fail("Expected exception for empty transaction list");
    });
  }

  @Test
  public void xirr_one_transaction() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // throws exception when only one transaction is passed
      new XirrCalculator(null).xirr(List.of(Cashflow.of(LocalDate.of(2010, 1, 1), -1000)));
      fail("Expected exception for only one transaction");
    });
  }

  @Test
  public void xirr_same_day() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // throws an exception when all transactions are on the same day
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new XirrCalculator(null, null).xirr(List.of(
          Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-01-01"), 2100)
      ));
      fail("Expected exception for all transactions on the same day");
    });
  }

  @Test
  public void xirr_all_negative() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // throws an exception when all transactions are negative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new XirrCalculator(BiSectionSolverBuilder.builder().build(), null).xirr(List.of(
          Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-05-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-09-01"), -2000)
      ));
      fail("Expected exception for all transactions are negative");
    });
  }

  @Test
  public void xirr_all_nonNegative() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      // throws an exception when all transactions are non-negative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new XirrCalculator(BiSectionSolverBuilder.builder().build(), null).xirr(List.of(
          Cashflow.of(LocalDate.parse("2010-01-01"), 1000),
          Cashflow.of(LocalDate.parse("2010-05-01"), 1000),
          Cashflow.of(LocalDate.parse("2010-09-01"), 0)
      ));
      fail("Expected exception for all transactions are non-negative");
    });
  }
}
