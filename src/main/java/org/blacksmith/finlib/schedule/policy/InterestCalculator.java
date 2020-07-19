package org.blacksmith.finlib.schedule.policy;

import java.util.List;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.schedule.ScheduleUpdater;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;

public class InterestCalculator implements ScheduleUpdater {
  private final ScheduleParameters scheduleParameters;

  public InterestCalculator(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  @Override
  public List<CashflowInterestEvent> apply(List<CashflowInterestEvent> cashflows) {
    for (CashflowInterestEvent cashflow : cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount interest = calculateInterest(cashflow);
        cashflow.setInterest(interest);
        cashflow.setAmount(interest);
      } else {
        cashflow.getSubEvents().forEach(rr -> {
          Amount interest = calculateInterest(rr);
          rr.setInterest(interest);
        });
        Amount interestSum = cashflow.getSubEvents().stream()
            .map(RateResetEvent::getInterest)
            .reduce(Amount.ZERO, Amount::add);
        cashflow.setInterest(interestSum);
        cashflow.setAmount(interestSum);
      }
    }
    return cashflows;
  }

  public Amount calculateInterest(InterestEventSrc ie) {
    double fraction = scheduleParameters.getBasis()
        .yearFraction(ie.getStartDate(), ie.getEndDate(), null);
    return Amount.of(ie.getPrincipal().doubleValue() * fraction * ie.getInterestRate().doubleValue() / 100d);
  }
}
