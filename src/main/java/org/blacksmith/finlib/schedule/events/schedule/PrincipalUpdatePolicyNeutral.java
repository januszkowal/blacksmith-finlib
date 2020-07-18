package org.blacksmith.finlib.schedule.events.schedule;

import org.blacksmith.finlib.basic.numbers.Amount;

public class PrincipalUpdatePolicyNeutral implements PrincipalUpdatePolicy {

  @Override
  public Amount apply(Amount prior) {
    return prior;
  }
}
