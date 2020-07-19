package org.blacksmith.finlib.schedule.policy;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.InterestEventSrc;

public class AbstractScheduleAlgorithmPolicy {
  protected final ScheduleParameters scheduleParameters;

  public AbstractScheduleAlgorithmPolicy(ScheduleParameters scheduleParameters) {
    this.scheduleParameters = scheduleParameters;
  }

  public Amount calculateInterest(InterestEventSrc ie) {
    double fraction = scheduleParameters.getBasis()
        .yearFraction(ie.getStartDate(), ie.getEndDate(), null);
    return Amount.of(ie.getPrincipal().doubleValue() * fraction * ie.getInterestRate().doubleValue() / 100d);
  }
}
