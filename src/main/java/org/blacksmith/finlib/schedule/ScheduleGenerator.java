package org.blacksmith.finlib.schedule;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.basic.numbers.Rate;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.ScheduleEvent;
import org.blacksmith.finlib.schedule.policy.AnnuityPolicy;

@Slf4j
public class ScheduleGenerator {

  private final ScheduleParameters scheduleParameters;

  List<XEvent> cashflows = new ArrayList<>();

  public ScheduleGenerator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

  public List<XEvent> generate(List<ScheduleEvent> events) {
    init(events);
    SchedulePolicy policy = new AnnuityPolicy(scheduleParameters,cashflows);
    policy.update();
    return cashflows;
  }

  private void init(List<ScheduleEvent> events) {
    cashflows.clear();
    for (ScheduleEvent event : events) {
      var cashflow = new XEvent();
      cashflow.setStartDate(event.getStartDate());
      cashflow.setEndDate(event.getEndDate());
      cashflow.setPaymentDate(event.getPaymentDate());
      //cashflow.setNotional(getNotional(event.getStartDate()));
      cashflow.setRate(Rate.of(scheduleParameters.getStartInterestRate()));
      cashflows.add(cashflow);
    }
  }


  public Amount getPrincipal(LocalDate date) {
    return scheduleParameters.getPrincipal();
  }

}
