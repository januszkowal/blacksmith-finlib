package org.blacksmith.finlib.interest.schedule.principal;

import org.blacksmith.finlib.basic.numbers.Amount;

public interface PrincipalUpdatePolicy {
  Amount apply(Amount prior);
}
