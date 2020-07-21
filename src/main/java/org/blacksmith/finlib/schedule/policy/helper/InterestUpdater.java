package org.blacksmith.finlib.schedule.policy.helper;

import java.util.List;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.schedule.policy.ScheduleUpdater;
import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.events.RateResetEvent;

public class InterestUpdater implements ScheduleUpdater {
  private final ScheduleParameters scheduleParameters;

  public InterestUpdater(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }
  @Override
  public List<InterestEvent> apply(List<InterestEvent> cashflows) {
    for (InterestEvent cashflow : cashflows) {
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

  public Amount calculateInterest(InterestEvent ie) {
    double fraction = scheduleParameters.getBasis()
        .yearFraction(ie.getStartDate(), ie.getEndDate(), null);
    return Amount.of(ie.getPrincipal().doubleValue() * fraction * ie.getInterestRate().doubleValue() / 100d);
  }

  public Amount calculateInterest(RateResetEvent ie) {
    double fraction = scheduleParameters.getBasis()
        .yearFraction(ie.getStartDate(), ie.getEndDate(), null);
    return Amount.of(ie.getPrincipal().doubleValue() * fraction * ie.getInterestRate().doubleValue() / 100d);
  }
}
