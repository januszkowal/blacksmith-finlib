package org.blacksmith.finlib.schedule.helper;

import java.util.List;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.commons.property.PropertyUpdater;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.ScheduleUpdater;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;

public class PrincipalUpdater implements ScheduleUpdater {
  private final PrincipalsHolder principalsHolder;

  public PrincipalUpdater(PrincipalsHolder principalsHolder) {
    this.principalsHolder = principalsHolder;
  }

  @Override
  public List<CashflowInterestEvent> apply(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    PropertyUpdater<CashflowInterestEvent, Amount> cashflowPrincipalUpdater =
        new PropertyUpdater<>(CashflowInterestEvent::getPrincipal,CashflowInterestEvent::setPrincipal);
    PropertyUpdater<RateResetEvent,Amount> rateResetPrincipalUpdater =
        new PropertyUpdater<>(RateResetEvent::getPrincipal,RateResetEvent::setPrincipal);
    for (CashflowInterestEvent cashflow: cashflows) {
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
