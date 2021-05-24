package org.blacksmith.finlib.interest.schedule.principal;

import org.blacksmith.finlib.basic.numbers.Amount;

public class PrincipalUpdatePolicyNeutral implements PrincipalUpdatePolicy {

  @Override
  public Amount apply(Amount prior) {
    return prior;
  }
}
