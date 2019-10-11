package org.blacksmith.finlib.schedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.blacksmith.finlib.basic.Amount;
import org.blacksmith.finlib.interestbasis.ScheduleInfo;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public class TermScheduleGenerator implements ScheduleGenerator {

  private final ScheduleParameters scheduleParameters;
  private Schedule schedule;

  public TermScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  
  
  @Override
  public Schedule generate() {
    this.schedule = new Schedule();
    ScheduleInfo scheduleInfo = createScheduleInfo(scheduleParameters,scheduleParameters.getStartDate(),scheduleParameters.getMaturityDate());
    double rate = 7 / 100.0;
    double interest =
        scheduleParameters.getBasis().yearFraction(scheduleInfo.getCouponStartDate(), scheduleInfo.getCouponEndDate(), scheduleInfo)*
        rate*
        scheduleInfo.getNotional().getValue().doubleValue();
    
    Cashflow cashflow = Cashflow.builder()
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .paymentDate(scheduleParameters.getMaturityDate())
        .notional(scheduleParameters.getNotional())
        .amount(Amount.of(interest))
        .build();
    schedule.getCashflow().add(cashflow);
    return schedule;
  }
}