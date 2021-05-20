package org.blacksmith.finlib.schedule.principal;

import org.blacksmith.finlib.basic.numbers.Amount;

public class PrincipalUpdatePolicyByAmount implements PrincipalUpdatePolicy {
  private final Amount minimumRemainingAmount;
  private final Amount subtrahent;

  public PrincipalUpdatePolicyByAmount(Amount minimumRemainingAmount, Amount subtrahent) {
    this.minimumRemainingAmount = minimumRemainingAmount;
    this.subtrahent = subtrahent;
  }

  @Override
  public Amount apply(Amount prior) {
    Amount calc = prior.subtract(subtrahent);
    return calc.compareTo(minimumRemainingAmount) >= 0 ? calc : minimumRemainingAmount;
  }
}
