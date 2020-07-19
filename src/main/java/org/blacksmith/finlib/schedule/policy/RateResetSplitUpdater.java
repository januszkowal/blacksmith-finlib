package org.blacksmith.finlib.schedule.policy;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.finlib.schedule.ScheduleUpdater;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;
import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.interest.RateResetEvent;
import org.blacksmith.finlib.schedule.events.schedule.PrincipalsHolder;
import org.blacksmith.finlib.schedule.events.schedule.SchedulePrincipalEvent;

public class RateResetSplitUpdater implements ScheduleUpdater {
  private final PrincipalsHolder principalsHolder;

  public RateResetSplitUpdater(PrincipalsHolder principalsHolder) {
    this.principalsHolder = principalsHolder;
  }

  @Override
  public List<CashflowInterestEvent> apply(List<CashflowInterestEvent> cashflows) {
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    //add splits
    if (!principalsHolder.isEmpty()) {
      for (SchedulePrincipalEvent pe : principalsHolder.getEvents()) {
        var ie = InterestEventSrc.getEventInRange(cashflows, pe.getDate());
        if (ie != null) {
          stateCounter.update(ie.splitSubEvent(pe.getDate()));
        }
      }
    }
    //remove splits
    for (CashflowInterestEvent cashflow: cashflows) {
      if (cashflow.getSubEvents().size()>1) {
        List<LocalDate> redudantResets = IntStream.range(1, cashflow.getSubEvents().size()).boxed()
            .map(rridx -> cashflow.getSubEvents().get(rridx))
            .filter(rr -> !rr.isRateReset())
            .filter(rr -> !principalsHolder.contains(rr.getStartDate()))
            .map(RateResetEvent::getStartDate)
            .collect(Collectors.toList());
        redudantResets.forEach(resetDate-> stateCounter.update(cashflow.consolidateSubEvent(resetDate)));
      }

    }
    return cashflows;
  }
}
