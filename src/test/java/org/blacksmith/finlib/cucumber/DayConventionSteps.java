package org.blacksmith.finlib.cucumber;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.StandardInterestBasis;

import groovy.lang.GroovyShell;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class DayConventionSteps {

  final GroovyShell shell = new GroovyShell();
  private StandardInterestBasis convention;
  private ScheduleInfo.ScheduleInfoBuilder scheduleInfoBuilder;

  @Given("Interest coupon - frequency {frequency} settlement date {date} maturity date {date}")
  public void setInterestCouponWithParameters(Frequency frequency, LocalDate settlementDate, LocalDate maturityDate) {
    this.scheduleInfoBuilder = ScheduleInfo.builder()
        .isEndOfMonthConvention(false)
        .couponFrequency(frequency)
        .startDate(settlementDate)
        .endDate(maturityDate);
  }

  @Given("Interest coupon simple")
  public void setInterestCouponSimple() {
    this.scheduleInfoBuilder = null;
  }

  @And("^For Day Convention (.*)$")
  public void setDayConvention(StandardInterestBasis convention) {
    this.convention = convention;
  }

  @Then("Day Convention verification")
  public void verifySchedule1(List<ConventionInput> input) {
    input.forEach(i -> {
      log.info("calculate row");
      assertEquals(i.getDays(), convention.days(i.getStartDate(), i.getEndDate(), i.scheduleInfo),
          () -> MessageFormat.format("days for startDate={0} endDate={1}", i.getStartDate(), i.getEndDate()));
      assertEquals(i.getFraction(), convention.yearFraction(i.getStartDate(), i.getEndDate(), i.scheduleInfo),
          () -> MessageFormat.format("fraction for startDate={0} endDate={1}", i.getStartDate(), i.getEndDate()));
    });
  }

  @DataTableType
  public List<ConventionInput> createCashflow(DataTable table) {
    return table.asMaps().stream()
        .map(fields -> {
          ScheduleInfo schInfo = null;
          if (convention == StandardInterestBasis.ACT_ACT_ICMA) {
            ArgChecker.notNull(scheduleInfoBuilder, "Schedule builder is null");
            schInfo = scheduleInfoBuilder
                .couponStartDate(LocalDate.parse(fields.get("start")))
                .couponEndDate(LocalDate.parse(fields.get("cend") == null ? fields.get("end") : fields.get("cend")))
                .build();
          }
          var builder = ConventionInput.builder()
              .startDate(LocalDate.parse(fields.get("start")))
              .endDate(LocalDate.parse(fields.get("end")))
              .days(Integer.parseInt(fields.get("days")))
              .fraction(calculateFraction(fields.get("fraction")))
              .scheduleInfo(schInfo);
          return builder.build();
        })
        .collect(Collectors.toList());
  }

  private double calculateFraction(String text) {
    Object result = shell.evaluate(text);
    if (result instanceof BigDecimal) {
      return ((BigDecimal) result).doubleValue();
    } else if (result instanceof Double) {
      return (Double) result;
    } else
      return 0d;
  }
}
