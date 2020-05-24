package org.blacksmith.finlib.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.math.solver.BiSectionAlgorithm;
import org.blacksmith.finlib.math.solver.NewtonRaphsonSolverBuilder;
import org.blacksmith.finlib.math.xirr.Cashflow;
import org.blacksmith.finlib.math.xirr.XirrCalculator;

@Slf4j
public class XirrCalculatorSteps {
  List<Cashflow> cashflows;
  double xirrBiCalcResult;
  double xirrNewtonRaphsonResult;
  @Given("Create schedule")
  public void certificationName(DataTable table) {
    this.xirrBiCalcResult = 0.0;
    this.xirrNewtonRaphsonResult = 0.0;
    this.cashflows = table.asMaps().stream()
        .map(fields-> Cashflow.of(LocalDate.parse(fields.get("on")),Double.parseDouble(fields.get("amount"))))
        .collect(Collectors.toList());
    log.info("Schedule:{}",cashflows);
  }

  @When("Xirr calculate")
  public void calculateXirr() {
    log.info("Calc BiSection");
    var calculatorBiCalc = new XirrCalculator(cashflows,
        BiSectionAlgorithm.builder()
            .withMinArg(-1)
            .withMaxArg(2), null);
    this.xirrBiCalcResult = calculatorBiCalc.xirr();
    log.info("Calc NewtonRaphson");
    var calculatorNewtonRapshon = new XirrCalculator(cashflows,
        NewtonRaphsonSolverBuilder.builder(), null);
    this.xirrNewtonRaphsonResult = calculatorNewtonRapshon.xirr();
  }

  @Then("Xirr result must be {double}")
  public void verifyResult(double result) {
    assertEquals(result,xirrBiCalcResult, 0.000_000_1,"BiSection result different");
    assertEquals(result,xirrNewtonRaphsonResult, 0.000_000_1, "NewtonRaphson result different");
  }
}
