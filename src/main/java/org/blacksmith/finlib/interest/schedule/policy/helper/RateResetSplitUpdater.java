package org.blacksmith.finlib.interest.schedule.policy.helper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.commons.counter.BooleanStateCounter;
import org.blacksmith.finlib.interest.schedule.events.InterestEvent;
import org.blacksmith.finlib.interest.schedule.events.PrincipalEvent;
import org.blacksmith.finlib.interest.schedule.events.RateResetEvent;
import org.blacksmith.finlib.interest.schedule.policy.ScheduleUpdater;
import org.blacksmith.finlib.interest.schedule.principal.PrincipalsHolder;

public class RateResetSplitUpdater implements ScheduleUpdater {
  private final PrincipalsHolder principalsHolder;

  public RateResetSplitUpdater(PrincipalsHolder principalsHolder) {
    this.principalsHolder = principalsHolder;
  }

  @Override
  public List<InterestEvent> apply(List<InterestEvent> cashflows) {
    BooleanStateCounter stateCounter = new BooleanStateCounter();
    //add splits
    if (!principalsHolder.isEmpty()) {
      for (PrincipalEvent pe : principalsHolder.getEvents()) {
        var ie = InterestEvent.getEventInRange(cashflows, pe.getDate());
        if (ie != null) {
          stateCounter.update(ie.splitSubEvent(pe.getDate()));
        }
      }
    }
    //remove splits
    for (InterestEvent cashflow : cashflows) {
      if (cashflow.getSubEvents().size() > 1) {
        List<LocalDate> redudantResets = IntStream.range(1, cashflow.getSubEvents().size())
            .mapToObj(rridx -> cashflow.getSubEvents().get(rridx))
            .filter(rr -> !rr.isRateReset())
            .filter(rr -> !principalsHolder.contains(rr.getStartDate()))
            .map(RateResetEvent::getStartDate)
            .collect(Collectors.toList());
        redudantResets.forEach(resetDate -> stateCounter.update(cashflow.consolidateSubEvent(resetDate)));
      }

    }
    return cashflows;
  }
}
