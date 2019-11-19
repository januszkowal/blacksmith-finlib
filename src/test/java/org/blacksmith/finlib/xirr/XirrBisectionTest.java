package org.blacksmith.finlib.xirr;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.blacksmith.commons.datetime.DateConversion;
import org.blacksmith.finlib.xirr.solver.BisectionAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.xirr.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class XirrBisectionTest {


  @Test
  public void xirr_1_year_no_growth() {
    // computes the xirr on 1 year growth of 0%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of( 1000, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of( 1100, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(  900, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet1() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-04-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-07-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-10-01")),
        Cashflow.of( 4300, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet2() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(1000, LocalDate.parse("2010-04-01")),
        Cashflow.of(1000, LocalDate.parse("2010-07-01")),
        Cashflow.of(1000, LocalDate.parse("2010-10-01")),
        Cashflow.of( -4300, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }
  @Test
  public void xirr_vs_spreadsheet_reordered1() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-10-01")),
        Cashflow.of( 4300, LocalDate.parse("2011-01-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-07-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(-1000, LocalDate.parse("2010-04-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet_reordered2() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(1000, LocalDate.parse("2010-10-01")),
        Cashflow.of(-4300, LocalDate.parse("2011-01-01")),
        Cashflow.of(1000, LocalDate.parse("2010-07-01")),
        Cashflow.of(1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(1000, LocalDate.parse("2010-04-01"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_over_100_percent_growth() {
    // computes rates of return greater than 100%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of( 3000, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(), null).xirr();
    assertEquals(2.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_one_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(    0, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_two_years() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(    0, LocalDate.parse("2011-01-01"))
    ), BisectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_half_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
        Cashflow.of(    0, LocalDate.parse("2010-07-01"))
    ), BisectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_readme_example() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(-1000, LocalDate.parse("2016-01-15")),
        Cashflow.of(-2500, LocalDate.parse("2016-02-08")),
        Cashflow.of(-1000, LocalDate.parse("2016-04-17")),
        Cashflow.of( 5050, LocalDate.parse("2016-08-24"))
    ), BisectionAlgorithm.builder(), null).xirr();
    assertEquals(0.2504234710540838, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue_from_node_js_version() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(-10000,  LocalDate.parse("2000-05-24")),
        Cashflow.of(3027.25, LocalDate.parse("2000-06-05")),
        Cashflow.of(630.68,  LocalDate.parse("2001-04-09")),
        Cashflow.of(2018.2,  LocalDate.parse("2004-02-24")),
        Cashflow.of(1513.62, LocalDate.parse("2005-03-18")),
        Cashflow.of(1765.89, LocalDate.parse("2006-02-15")),
        Cashflow.of(4036.33, LocalDate.parse("2007-01-10")),
        Cashflow.of(4036.33, LocalDate.parse("2007-11-14")),
        Cashflow.of(1513.62, LocalDate.parse("2008-12-17")),
        Cashflow.of(1513.62, LocalDate.parse("2010-01-15")),
        Cashflow.of(2018.16, LocalDate.parse("2011-01-14")),
        Cashflow.of(1513.62, LocalDate.parse("2012-02-03")),
        Cashflow.of(1009.08, LocalDate.parse("2013-01-18")),
        Cashflow.of(1513.62, LocalDate.parse("2014-01-24")),
        Cashflow.of(1513.62, LocalDate.parse("2015-01-30")),
        Cashflow.of(1765.89, LocalDate.parse("2016-01-22")),
        Cashflow.of(1765.89, LocalDate.parse("2017-01-20")),
        Cashflow.of(22421.55, LocalDate.parse("2017-06-05"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(0.2126861, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5a1() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(-2610, LocalDate.parse("2001-06-22")),
        Cashflow.of(-2589, LocalDate.parse("2001-07-03")),
        Cashflow.of(-5110, LocalDate.parse("2001-07-05")),
        Cashflow.of(-2550, LocalDate.parse("2001-07-06")),
        Cashflow.of(-5086, LocalDate.parse("2001-07-09")),
        Cashflow.of(-2561, LocalDate.parse("2001-07-10")),
        Cashflow.of(-5040, LocalDate.parse("2001-07-12")),
        Cashflow.of(-2552, LocalDate.parse("2001-07-13")),
        Cashflow.of(-2530, LocalDate.parse("2001-07-16")),
        Cashflow.of(29520, LocalDate.parse("2001-07-17"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }
  @Test
  public void xirr_issue5a2() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(2610, LocalDate.parse("2001-06-22")),
        Cashflow.of(2589, LocalDate.parse("2001-07-03")),
        Cashflow.of(5110, LocalDate.parse("2001-07-05")),
        Cashflow.of(2550, LocalDate.parse("2001-07-06")),
        Cashflow.of(5086, LocalDate.parse("2001-07-09")),
        Cashflow.of(2561, LocalDate.parse("2001-07-10")),
        Cashflow.of(5040, LocalDate.parse("2001-07-12")),
        Cashflow.of(2552, LocalDate.parse("2001-07-13")),
        Cashflow.of(2530, LocalDate.parse("2001-07-16")),
        Cashflow.of(-29520, LocalDate.parse("2001-07-17"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b1() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(-2610, LocalDate.parse("2001-06-22")),
        Cashflow.of(-2589, LocalDate.parse("2001-07-03")),
        Cashflow.of(-5110, LocalDate.parse("2001-07-05")),
        Cashflow.of(-2550, LocalDate.parse("2001-07-06")),
        Cashflow.of(-5086, LocalDate.parse("2001-07-09")),
        Cashflow.of(-2561, LocalDate.parse("2001-07-10")),
        Cashflow.of(-5040, LocalDate.parse("2001-07-12")),
        Cashflow.of(-2552, LocalDate.parse("2001-07-13")),
        Cashflow.of(-2530, LocalDate.parse("2001-07-16")),
        Cashflow.of(-9840, LocalDate.parse("2001-07-17")),
        Cashflow.of(38900, LocalDate.parse("2001-07-18"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.8353404, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b2() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(2610, LocalDate.parse("2001-06-22")),
        Cashflow.of(2589, LocalDate.parse("2001-07-03")),
        Cashflow.of(5110, LocalDate.parse("2001-07-05")),
        Cashflow.of(2550, LocalDate.parse("2001-07-06")),
        Cashflow.of(5086, LocalDate.parse("2001-07-09")),
        Cashflow.of(2561, LocalDate.parse("2001-07-10")),
        Cashflow.of(5040, LocalDate.parse("2001-07-12")),
        Cashflow.of(2552, LocalDate.parse("2001-07-13")),
        Cashflow.of(2530, LocalDate.parse("2001-07-16")),
        Cashflow.of(9840, LocalDate.parse("2001-07-17")),
        Cashflow.of(-38900, LocalDate.parse("2001-07-18"))
    ), BisectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.8353404, rate, TOLERANCE);
  }

  @Test
  public void xirr_no_transactions() {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws exception when no transactions are passed
      new Xirr(Collections.emptyList()).xirr();
      fail("Expected exception for empty transaction list");
    });
  }

  @Test
  public void xirr_one_transaction() {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws exception when only one transaction is passed
      new Xirr(Cashflow.of(-1000, LocalDate.of(2010,01,01))).xirr();
      fail("Expected exception for only one transaction");

    });
  }

  @Test
  public void xirr_same_day() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are on the same day
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(-1000, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 09:00"))),
          Cashflow.of(-1000, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 12:00"))),
          Cashflow.of( 2100, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 15:00")))
      ), BisectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions on the same day");
    });
  }

  @Test
  public void xirr_all_negative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are negative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(-1000, LocalDate.parse("2010-01-01")),
          Cashflow.of(-1000, LocalDate.parse("2010-05-01")),
          Cashflow.of(-2000, LocalDate.parse("2010-09-01"))
      ), BisectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions are negative");
    });
  }

  @Test
  public void xirr_all_nonnegative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are nonnegative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(1000, LocalDate.of(2010,01,01)),
          Cashflow.of(1000, LocalDate.parse("2010-05-01")),
          Cashflow.of(   0, LocalDate.parse("2010-09-01"))
      ), BisectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions are nonnegative");
    });
  }
}
