package org.blacksmith.finlib.cucumber;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.math.solver.BiSectionSolverBuilder;
import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.valuation.dto.Cashflow;
import org.blacksmith.finlib.valuation.xirr.XirrCalculatorBuilder;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class XirrCalculatorSteps {

  List<Cashflow> cashflows;
  double xirrBiCalcResult;
  double xirrNewtonRaphsonResult;

  /* create whole table */
  @DataTableType
  public List<Cashflow> createCashflow(DataTable table) {
    return table.asMaps().stream()
        .map(fields -> Cashflow.of(LocalDate.parse(fields.get("on")), Double.parseDouble(fields.get("amount"))))
        .collect(Collectors.toList());
  }

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
        .withSolverFunction(BiSectionSolverBuilder.builder()
            .minArg(-1)
            .maxArg(2)
            .build())
        .build();
    this.xirrBiCalcResult = calculatorBiCalc.xirr(cashflows);
    log.info("Calc BiSection={}", this.xirrBiCalcResult);
    log.info("Calc NewtonRaphson");
    var calculatorNewtonRapshon = XirrCalculatorBuilder.builder()
        .withSolverFunctionDerivative(NewtonRaphsonSolverBuilder.builder().build())
        .build();
    this.xirrNewtonRaphsonResult = calculatorNewtonRapshon.xirr(cashflows);
    log.info("Calc NewtonRaphson={}", this.xirrNewtonRaphsonResult);
  }

  @Then("Xirr result must be {double}")
  public void verifyResult(double result) {
    assertEquals(result, xirrBiCalcResult, 0.000_000_1, "BiSection result different");
    assertEquals(result, xirrNewtonRaphsonResult, 0.000_000_1, "NewtonRaphson result different");
  }
}
