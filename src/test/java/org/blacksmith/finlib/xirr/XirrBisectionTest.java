package org.blacksmith.finlib.xirr;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import org.blacksmith.commons.datetime.DateConversion;
import org.blacksmith.finlib.xirr.solver.BisectionMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.xirr.solver.BisectionMethod.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class XirrBisectionTest {


  @Test
  public void xirr_1_year_no_growth() {
    // computes the xirr on 1 year growth of 0%
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow( 1000, LocalDate.parse("2011-01-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow( 1100, LocalDate.parse("2011-01-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(  900, LocalDate.parse("2011-01-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet1() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(-1000, LocalDate.parse("2010-04-01")),
        new Cashflow(-1000, LocalDate.parse("2010-07-01")),
        new Cashflow(-1000, LocalDate.parse("2010-10-01")),
        new Cashflow( 4300, LocalDate.parse("2011-01-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet2() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(1000, LocalDate.parse("2010-01-01")),
        new Cashflow(1000, LocalDate.parse("2010-04-01")),
        new Cashflow(1000, LocalDate.parse("2010-07-01")),
        new Cashflow(1000, LocalDate.parse("2010-10-01")),
        new Cashflow( -4300, LocalDate.parse("2011-01-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }
  @Test
  public void xirr_vs_spreadsheet_reordered1() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-10-01")),
        new Cashflow( 4300, LocalDate.parse("2011-01-01")),
        new Cashflow(-1000, LocalDate.parse("2010-07-01")),
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(-1000, LocalDate.parse("2010-04-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet_reordered2() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(1000, LocalDate.parse("2010-10-01")),
        new Cashflow(-4300, LocalDate.parse("2011-01-01")),
        new Cashflow(1000, LocalDate.parse("2010-07-01")),
        new Cashflow(1000, LocalDate.parse("2010-01-01")),
        new Cashflow(1000, LocalDate.parse("2010-04-01"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_over_100_percent_growth() {
    // computes rates of return greater than 100%
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow( 3000, LocalDate.parse("2011-01-01"))
    )).xirr();
    assertEquals(2.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_one_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(    0, LocalDate.parse("2011-01-01"))
    )).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_two_years() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(    0, LocalDate.parse("2011-01-01"))
    )).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_half_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        new Cashflow(-1000, LocalDate.parse("2010-01-01")),
        new Cashflow(    0, LocalDate.parse("2010-07-01"))
    )).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_readme_example() {
    double rate = new Xirr(
        new Cashflow(-1000, LocalDate.parse("2016-01-15")),
        new Cashflow(-2500, LocalDate.parse("2016-02-08")),
        new Cashflow(-1000, LocalDate.parse("2016-04-17")),
        new Cashflow( 5050, LocalDate.parse("2016-08-24"))
    ).xirr();
    assertEquals(0.2504234710540838, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue_from_node_js_version() {
    double rate = new Xirr(Arrays.asList(
        new Cashflow(-10000,  LocalDate.parse("2000-05-24")),
        new Cashflow(3027.25, LocalDate.parse("2000-06-05")),
        new Cashflow(630.68,  LocalDate.parse("2001-04-09")),
        new Cashflow(2018.2,  LocalDate.parse("2004-02-24")),
        new Cashflow(1513.62, LocalDate.parse("2005-03-18")),
        new Cashflow(1765.89, LocalDate.parse("2006-02-15")),
        new Cashflow(4036.33, LocalDate.parse("2007-01-10")),
        new Cashflow(4036.33, LocalDate.parse("2007-11-14")),
        new Cashflow(1513.62, LocalDate.parse("2008-12-17")),
        new Cashflow(1513.62, LocalDate.parse("2010-01-15")),
        new Cashflow(2018.16, LocalDate.parse("2011-01-14")),
        new Cashflow(1513.62, LocalDate.parse("2012-02-03")),
        new Cashflow(1009.08, LocalDate.parse("2013-01-18")),
        new Cashflow(1513.62, LocalDate.parse("2014-01-24")),
        new Cashflow(1513.62, LocalDate.parse("2015-01-30")),
        new Cashflow(1765.89, LocalDate.parse("2016-01-22")),
        new Cashflow(1765.89, LocalDate.parse("2017-01-20")),
        new Cashflow(22421.55, LocalDate.parse("2017-06-05"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(0.2126861, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5a1() {
    double rate = new Xirr(Arrays.asList(
        new Cashflow(-2610, LocalDate.parse("2001-06-22")),
        new Cashflow(-2589, LocalDate.parse("2001-07-03")),
        new Cashflow(-5110, LocalDate.parse("2001-07-05")),
        new Cashflow(-2550, LocalDate.parse("2001-07-06")),
        new Cashflow(-5086, LocalDate.parse("2001-07-09")),
        new Cashflow(-2561, LocalDate.parse("2001-07-10")),
        new Cashflow(-5040, LocalDate.parse("2001-07-12")),
        new Cashflow(-2552, LocalDate.parse("2001-07-13")),
        new Cashflow(-2530, LocalDate.parse("2001-07-16")),
        new Cashflow(29520, LocalDate.parse("2001-07-17"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }
  @Test
  public void xirr_issue5a2() {
    double rate = new Xirr(Arrays.asList(
        new Cashflow(2610, LocalDate.parse("2001-06-22")),
        new Cashflow(2589, LocalDate.parse("2001-07-03")),
        new Cashflow(5110, LocalDate.parse("2001-07-05")),
        new Cashflow(2550, LocalDate.parse("2001-07-06")),
        new Cashflow(5086, LocalDate.parse("2001-07-09")),
        new Cashflow(2561, LocalDate.parse("2001-07-10")),
        new Cashflow(5040, LocalDate.parse("2001-07-12")),
        new Cashflow(2552, LocalDate.parse("2001-07-13")),
        new Cashflow(2530, LocalDate.parse("2001-07-16")),
        new Cashflow(-29520, LocalDate.parse("2001-07-17"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b1() {
    double rate = new Xirr(Arrays.asList(
        new Cashflow(-2610, LocalDate.parse("2001-06-22")),
        new Cashflow(-2589, LocalDate.parse("2001-07-03")),
        new Cashflow(-5110, LocalDate.parse("2001-07-05")),
        new Cashflow(-2550, LocalDate.parse("2001-07-06")),
        new Cashflow(-5086, LocalDate.parse("2001-07-09")),
        new Cashflow(-2561, LocalDate.parse("2001-07-10")),
        new Cashflow(-5040, LocalDate.parse("2001-07-12")),
        new Cashflow(-2552, LocalDate.parse("2001-07-13")),
        new Cashflow(-2530, LocalDate.parse("2001-07-16")),
        new Cashflow(-9840, LocalDate.parse("2001-07-17")),
        new Cashflow(38900, LocalDate.parse("2001-07-18"))
    ), BisectionMethod.builder(),null).xirr();
    assertEquals(-0.8353404, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b2() {
    double rate = new Xirr(Arrays.asList(
        new Cashflow(2610, LocalDate.parse("2001-06-22")),
        new Cashflow(2589, LocalDate.parse("2001-07-03")),
        new Cashflow(5110, LocalDate.parse("2001-07-05")),
        new Cashflow(2550, LocalDate.parse("2001-07-06")),
        new Cashflow(5086, LocalDate.parse("2001-07-09")),
        new Cashflow(2561, LocalDate.parse("2001-07-10")),
        new Cashflow(5040, LocalDate.parse("2001-07-12")),
        new Cashflow(2552, LocalDate.parse("2001-07-13")),
        new Cashflow(2530, LocalDate.parse("2001-07-16")),
        new Cashflow(9840, LocalDate.parse("2001-07-17")),
        new Cashflow(-38900, LocalDate.parse("2001-07-18"))
    ), BisectionMethod.builder(),null).xirr();
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
      new Xirr(new Cashflow(-1000, LocalDate.of(2010,01,01))).xirr();
      fail("Expected exception for only one transaction");

    });
  }

  @Test
  public void xirr_same_day() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are on the same day
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          new Cashflow(-1000, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 09:00"))),
          new Cashflow(-1000, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 12:00"))),
          new Cashflow( 2100, DateConversion.convertDateToLocalDate(format.parse("2010-01-01 15:00")))
      ), BisectionMethod.builder(),null).xirr();
      fail("Expected exception for all transactions on the same day");
    });
  }

  @Test
  public void xirr_all_negative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are negative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          new Cashflow(-1000, LocalDate.parse("2010-01-01")),
          new Cashflow(-1000, LocalDate.parse("2010-05-01")),
          new Cashflow(-2000, LocalDate.parse("2010-09-01"))
      ), BisectionMethod.builder(),null).xirr();
      fail("Expected exception for all transactions are negative");
    });
  }

  @Test
  public void xirr_all_nonnegative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are nonnegative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          new Cashflow(1000, LocalDate.of(2010,01,01)),
          new Cashflow(1000, LocalDate.parse("2010-05-01")),
          new Cashflow(   0, LocalDate.parse("2010-09-01"))
      ), BisectionMethod.builder(),null).xirr();
      fail("Expected exception for all transactions are nonnegative");
    });
  }
}
