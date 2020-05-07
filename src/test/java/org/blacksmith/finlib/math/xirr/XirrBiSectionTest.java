package org.blacksmith.finlib.math.xirr;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.blacksmith.commons.datetime.DateConversion;
import org.blacksmith.finlib.math.solver.BiSectionAlgorithm;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.Xirr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.blacksmith.finlib.math.solver.AbstractSolverBuilder.TOLERANCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class XirrBiSectionTest {


  @Test
  public void xirr_1_year_no_growth() {
    // computes the xirr on 1 year growth of 0%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 1000)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_growth() {
    // computes the xirr on 1 year growth of 10%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 1100)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_1_year_decline() {
    // computes the negative xirr on 1 year decline of 10%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"),  900)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.10, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet1() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-04-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-07-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-10-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"), 4300)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet2() {
    // computes the xirr on a particular data set the same as a popular
    // spreadsheet
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),1000),
        Cashflow.of(LocalDate.parse("2010-04-01"),1000),
        Cashflow.of(LocalDate.parse("2010-07-01"),1000),
        Cashflow.of(LocalDate.parse("2010-10-01"),1000),
        Cashflow.of(LocalDate.parse("2011-01-01"),-4300)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }
  @Test
  public void xirr_vs_spreadsheet_reordered1() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-10-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"),4300),
        Cashflow.of(LocalDate.parse("2010-07-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-04-01"),-1000)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_vs_spreadsheet_reordered2() {
    // gets the same answer even if the transations are out of order
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-10-01"),1000),
        Cashflow.of(LocalDate.parse("2011-01-01"),-4300),
        Cashflow.of(LocalDate.parse("2010-07-01"),1000),
        Cashflow.of(LocalDate.parse("2010-01-01"),1000),
        Cashflow.of(LocalDate.parse("2010-04-01"),1000)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.1212676, xirr, TOLERANCE);
  }

  @Test
  public void xirr_over_100_percent_growth() {
    // computes rates of return greater than 100%
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of( LocalDate.parse("2011-01-01"),3000)
    ), BiSectionAlgorithm.builder(), null).xirr();
    assertEquals(2.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_one_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(    LocalDate.parse("2011-01-01"),0)
    ), BiSectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_two_years() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2011-01-01"),0)
    ), BiSectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_total_loss_half_year() {
    // computes a rate of return of -100% on a total loss
    final double xirr = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2010-01-01"),-1000),
        Cashflow.of(LocalDate.parse("2010-07-01"),0)
    ), BiSectionAlgorithm.builder(), null).xirr();
    assertEquals(-1.00, xirr, TOLERANCE);
  }

  @Test
  public void xirr_readme_example() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2016-01-15"),-1000),
        Cashflow.of(LocalDate.parse("2016-02-08"),-2500),
        Cashflow.of(LocalDate.parse("2016-04-17"),-1000),
        Cashflow.of(LocalDate.parse("2016-08-24"),5050)
    ), BiSectionAlgorithm.builder(), null).xirr();
    assertEquals(0.2504234710540838, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue_from_node_js_version() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2000-05-24"),-10000),
        Cashflow.of(LocalDate.parse("2000-06-05"), 3027.25),
        Cashflow.of(LocalDate.parse("2001-04-09"),  630.68),
        Cashflow.of(LocalDate.parse("2004-02-24"),  2018.2),
        Cashflow.of(LocalDate.parse("2005-03-18"), 1513.62),
        Cashflow.of(LocalDate.parse("2006-02-15"), 1765.89),
        Cashflow.of(LocalDate.parse("2007-01-10"), 4036.33),
        Cashflow.of(LocalDate.parse("2007-11-14"), 4036.33),
        Cashflow.of(LocalDate.parse("2008-12-17"), 1513.62),
        Cashflow.of(LocalDate.parse("2010-01-15"), 1513.62),
        Cashflow.of(LocalDate.parse("2011-01-14"), 2018.16),
        Cashflow.of(LocalDate.parse("2012-02-03"), 1513.62),
        Cashflow.of(LocalDate.parse("2013-01-18"), 1009.08),
        Cashflow.of(LocalDate.parse("2014-01-24"), 1513.62),
        Cashflow.of(LocalDate.parse("2015-01-30"), 1513.62),
        Cashflow.of(LocalDate.parse("2016-01-22"), 1765.89),
        Cashflow.of(LocalDate.parse("2017-01-20"), 1765.89),
        Cashflow.of(LocalDate.parse("2017-06-05"), 22421.55)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(0.2126861, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5a1() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2001-06-22"), -2610),
        Cashflow.of(LocalDate.parse("2001-07-03"), -2589),
        Cashflow.of(LocalDate.parse("2001-07-05"), -5110),
        Cashflow.of(LocalDate.parse("2001-07-06"), -2550),
        Cashflow.of(LocalDate.parse("2001-07-09"), -5086),
        Cashflow.of(LocalDate.parse("2001-07-10"), -2561),
        Cashflow.of(LocalDate.parse("2001-07-12"), -5040),
        Cashflow.of(LocalDate.parse("2001-07-13"), -2552),
        Cashflow.of(LocalDate.parse("2001-07-16"), -2530),
        Cashflow.of(LocalDate.parse("2001-07-17"), 29520)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }
  @Test
  public void xirr_issue5a2() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2001-06-22"), 2610),
        Cashflow.of(LocalDate.parse("2001-07-03"), 2589),
        Cashflow.of(LocalDate.parse("2001-07-05"), 5110),
        Cashflow.of(LocalDate.parse("2001-07-06"), 2550),
        Cashflow.of(LocalDate.parse("2001-07-09"), 5086),
        Cashflow.of(LocalDate.parse("2001-07-10"), 2561),
        Cashflow.of(LocalDate.parse("2001-07-12"), 5040),
        Cashflow.of(LocalDate.parse("2001-07-13"), 2552),
        Cashflow.of(LocalDate.parse("2001-07-16"), 2530),
        Cashflow.of(LocalDate.parse("2001-07-17"), -29520)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.7640294, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b1() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2001-06-22"), -2610),
        Cashflow.of(LocalDate.parse("2001-07-03"), -2589),
        Cashflow.of(LocalDate.parse("2001-07-05"), -5110),
        Cashflow.of(LocalDate.parse("2001-07-06"), -2550),
        Cashflow.of(LocalDate.parse("2001-07-09"), -5086),
        Cashflow.of(LocalDate.parse("2001-07-10"), -2561),
        Cashflow.of(LocalDate.parse("2001-07-12"), -5040),
        Cashflow.of(LocalDate.parse("2001-07-13"), -2552),
        Cashflow.of(LocalDate.parse("2001-07-16"), -2530),
        Cashflow.of(LocalDate.parse("2001-07-17"), -9840),
        Cashflow.of(LocalDate.parse("2001-07-18"), 38900)
    ), BiSectionAlgorithm.builder(),null).xirr();
    assertEquals(-0.8353404, rate, TOLERANCE);
  }

  @Test
  public void xirr_issue5b2() {
    double rate = new Xirr(Arrays.asList(
        Cashflow.of(LocalDate.parse("2001-06-22"), 2610),
        Cashflow.of(LocalDate.parse("2001-07-03"), 2589),
        Cashflow.of(LocalDate.parse("2001-07-05"), 5110),
        Cashflow.of(LocalDate.parse("2001-07-06"), 2550),
        Cashflow.of(LocalDate.parse("2001-07-09"), 5086),
        Cashflow.of(LocalDate.parse("2001-07-10"), 2561),
        Cashflow.of(LocalDate.parse("2001-07-12"), 5040),
        Cashflow.of(LocalDate.parse("2001-07-13"), 2552),
        Cashflow.of(LocalDate.parse("2001-07-16"), 2530),
        Cashflow.of(LocalDate.parse("2001-07-17"), 9840),
        Cashflow.of(LocalDate.parse("2001-07-18"), -38900)
    ), BiSectionAlgorithm.builder(),null).xirr();
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
      new Xirr(List.of(Cashflow.of(LocalDate.of(2010,01,01), -1000))).xirr();
      fail("Expected exception for only one transaction");

    });
  }

  @Test
  public void xirr_same_day() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are on the same day
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(DateConversion.convertDateToLocalDate(format.parse("2010-01-01 09:00")), -1000),
          Cashflow.of(DateConversion.convertDateToLocalDate(format.parse("2010-01-01 12:00")), -1000),
          Cashflow.of(DateConversion.convertDateToLocalDate(format.parse("2010-01-01 15:00")), 2100)
      ), BiSectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions on the same day");
    });
  }

  @Test
  public void xirr_all_negative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are negative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(LocalDate.parse("2010-01-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-05-01"), -1000),
          Cashflow.of(LocalDate.parse("2010-09-01"), -2000)
      ), BiSectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions are negative");
    });
  }

  @Test
  public void xirr_all_nonnegative() throws Exception {
    Assertions.assertThrows(IllegalArgumentException.class,()->{
      // throws an exception when all transactions are nonnegative
      final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      new Xirr(Arrays.asList(
          Cashflow.of(LocalDate.parse("2010-01-01"), 1000),
          Cashflow.of(LocalDate.parse("2010-05-01"), 1000),
          Cashflow.of(LocalDate.parse("2010-09-01"), 0)
      ), BiSectionAlgorithm.builder(),null).xirr();
      fail("Expected exception for all transactions are nonnegative");
    });
  }
}
