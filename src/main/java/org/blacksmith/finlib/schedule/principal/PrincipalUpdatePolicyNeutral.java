package org.blacksmith.finlib.schedule.principal;

import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.schedule.principal.PrincipalUpdatePolicy;

public class PrincipalUpdatePolicyNeutral implements PrincipalUpdatePolicy {

  @Override
  public Amount apply(Amount prior) {
    return prior;
  }
}
