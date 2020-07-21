package org.blacksmith.finlib.schedule.policy.helper;

import java.util.List;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.commons.property.PropertyUpdater;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.events.RateResetEvent;
import org.blacksmith.finlib.schedule.principal.PrincipalsHolder;
import org.blacksmith.finlib.schedule.policy.ScheduleUpdater;

public class PrincipalUpdater implements ScheduleUpdater {
  private final PrincipalsHolder principalsHolder;

  public PrincipalUpdater(PrincipalsHolder principalsHolder) {
    this.principalsHolder = principalsHolder;
  }

  @Override
  public List<InterestEvent> apply(List<InterestEvent> cashflows) {
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    PropertyUpdater<InterestEvent, Amount> cashflowPrincipalUpdater =
        new PropertyUpdater<>(InterestEvent::getPrincipal, InterestEvent::setPrincipal);
    PropertyUpdater<RateResetEvent,Amount> rateResetPrincipalUpdater =
        new PropertyUpdater<>(RateResetEvent::getPrincipal,RateResetEvent::setPrincipal);
    for (InterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().isEmpty()) {
        Amount newPrincipal = principalsHolder.getPrincipal(cashflow.getStartDate());
        stateCounter.update(cashflowPrincipalUpdater.set(cashflow,newPrincipal));
      }
      else {
        for (RateResetEvent rr: cashflow.getSubEvents()) {
          Amount newPrincipal = principalsHolder.getPrincipal(rr.getStartDate());
          stateCounter.update(rateResetPrincipalUpdater.set(rr,newPrincipal));
        }
        stateCounter.update(cashflowPrincipalUpdater.set(cashflow,cashflow.firstRateReset().getPrincipal()));
      }
    }
    return cashflows;
  }
}
