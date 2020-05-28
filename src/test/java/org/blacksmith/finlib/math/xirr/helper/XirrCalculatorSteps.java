package org.blacksmith.finlib.math.xirr.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.math.solver.BiSectionSolverBuilder;
import org.blacksmith.finlib.math.solver.Function1stDerivative;
import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.XirrCalculatorBuilder;

@Slf4j
public class XirrCalculatorSteps {

  List<Cashflow> cashflows;
  double xirrBiCalcResult;
  double xirrNewtonRaphsonResult;

  /* create single row*/
//  @DataTableType
//  public Cashflow createCashflow(Map<String, String> entry) {
//    return Cashflow.of(LocalDate.parse(entry.get("on")),Double.parseDouble(entry.get("amount")));
//  }

  /* create whole table */
  @DataTableType
  public List<Cashflow> createCashflow(DataTable table) {
    return table.asMaps().stream()
        .map(fields -> Cashflow.of(LocalDate.parse(fields.get("on")), Double.parseDouble(fields.get("amount"))))
        .collect(Collectors.toList());
  }

  /* creating cashflows inside */
//  @Given("Create schedule")
//  public void createScheduleName(DataTable table) {
//    this.xirrBiCalcResult = 0.0;
//    this.xirrNewtonRaphsonResult = 0.0;
//    this.cashflows = table.asMaps().stream()
//        .map(fields -> Cashflow.of(LocalDate.parse(fields.get("on")), Double.parseDouble(fields.get("amount"))))
//        .collect(Collectors.toList());
//    log.info("Schedule:{}", cashflows);
//  }

  @Given("Create schedule")
  public void createSchedule(List<Cashflow> cashflows) {
    this.xirrBiCalcResult = 0.0;
    this.xirrNewtonRaphsonResult = 0.0;
    this.cashflows = cashflows;
    log.info("Schedule:{}", cashflows);
  }

  @When("Xirr calculate")
  public void calculateXirr() {
    log.info("Calc BiSection");
    var calculatorBiCalc = XirrCalculatorBuilder.builder()
        .withSolverBuilder(BiSectionSolverBuilder.builder()
            .withMinArg(-1)
            .withMaxArg(2))
        .withCashflows(cashflows).build();
    this.xirrBiCalcResult = calculatorBiCalc.xirr();
    log.info("Calc NewtonRaphson");
    var calculatorNewtonRapshon = XirrCalculatorBuilder.<Function1stDerivative>builder()
        .withCashflows(cashflows)
        .withSolverBuilder(NewtonRaphsonSolverBuilder.builder()).build();
    this.xirrNewtonRaphsonResult = calculatorNewtonRapshon.xirr();
  }

  @Then("Xirr result must be {double}")
  public void verifyResult(double result) {
    assertEquals(result, xirrBiCalcResult, 0.000_000_1, "BiSection result different");
    assertEquals(result, xirrNewtonRaphsonResult, 0.000_000_1, "NewtonRaphson result different");
  }
}
