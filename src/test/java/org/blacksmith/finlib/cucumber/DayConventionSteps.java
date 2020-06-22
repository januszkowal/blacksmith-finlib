package org.blacksmith.finlib.cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;

import groovy.lang.GroovyShell;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.is.En;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.commons.arg.Validate;
import org.blacksmith.commons.datetime.TimeUnit;
import org.blacksmith.finlib.datetime.Frequency;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.StandardDayCountConvention;

@Slf4j
public class DayConventionSteps {

  private StandardDayCountConvention convention;
  final GroovyShell shell = new GroovyShell();
  private ScheduleInfo.ScheduleInfoBuilder scheduleInfoBuilder;

  @Given("Interest accrual - frequency {frequency} settlement date {date} maturity date {date}")
  public void setScheduleInfo1(Frequency frequency, LocalDate settlementDate, LocalDate maturityDate) {
    this.scheduleInfoBuilder = ScheduleInfo.builder()
        .isEndOfMonthConvention(false)
        .couponFrequency(frequency)
        .startDate(settlementDate)
        .endDate(maturityDate);
  }

  @And("^For Day Convention (.*)$")
  public void setDayConvention1(StandardDayCountConvention convention) {
    this.convention = convention;
  }

  @Given("^Day Convention is (.*)$")
  public void setDayConvention(StandardDayCountConvention convention) {
    this.convention = convention;
    //this.scheduleInfoBuilder = null;
  }
  @And("Schedule parameters frequency {frequency} settlement date {date} maturity date {date}")
  public void setScheduleInfo(Frequency frequency, LocalDate settlementDate, LocalDate maturityDate) {
    this.scheduleInfoBuilder = ScheduleInfo.builder()
        .isEndOfMonthConvention(false)
        .couponFrequency(frequency)
        .startDate(settlementDate)
        .endDate(maturityDate);
  }

  @Then("Day Convention verification")
  public void verifySchedule1(List<ConventionInput> input) {
    input.forEach(i->{
      log.info("calculate row");
      assertEquals(i.getDays(),convention.days(i.getStartDate(),i.getEndDate(),i.scheduleInfo),()-> MessageFormat.format("days for startDate={0} endDate={1}",i.getStartDate(),i.getEndDate()));
      assertEquals(i.getFraction(),convention.yearFraction(i.getStartDate(),i.getEndDate(),i.scheduleInfo),()-> MessageFormat.format("fraction for startDate={0} endDate={1}",i.getStartDate(),i.getEndDate()));
    });
  }


  @DataTableType
  public List<ConventionInput> createCashflow(DataTable table) {
    return table.asMaps().stream()
        .map(fields-> {
          ScheduleInfo schInfo = null;
          if (convention==StandardDayCountConvention.ACT_ACT_ISMA) {
            Validate.notNull(scheduleInfoBuilder,"Schedule builder is null");
            schInfo = scheduleInfoBuilder
                .couponStartDate(LocalDate.parse(fields.get("start")))
                .couponEndDate(LocalDate.parse(fields.get("cend")==null ? fields.get("end") : fields.get("cend")))
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
      return ((BigDecimal)result).doubleValue();
    }
    else if (result instanceof Double) {
      return (Double) result;
    }
    else return 0d;
  }
}
