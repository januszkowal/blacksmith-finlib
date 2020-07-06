package org.blacksmith.finlib.schedule.policy;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.XEvent;

public class AbstractPolicy {
  protected final ScheduleParameters scheduleParameters;
  protected final List<XEvent> cashflows;

  public AbstractPolicy(ScheduleParameters scheduleParameters, List<XEvent> cashflows) {
    this.scheduleParameters = scheduleParameters;
    this.cashflows = cashflows;
  }

  public Amount calculateCouponInterest(XEvent cashflow) {
    double fraction = scheduleParameters.getBasis()
        .yearFraction(cashflow.getStartDate(), cashflow.getEndDate(), null);
    return Amount.of(cashflow.getPrincipal().doubleValue() * fraction * cashflow.getRate().doubleValue() / 100d);
  }

  protected String cashflowsToString() {
    return cashflows.stream()
        .map(XEvent::toString)
        .collect(Collectors.joining("\n","\n",""));
  }
}
