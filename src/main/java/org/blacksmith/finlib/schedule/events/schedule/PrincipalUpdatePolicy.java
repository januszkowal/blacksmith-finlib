package org.blacksmith.finlib.schedule.events.schedule;

import org.blacksmith.finlib.basic.numbers.Amount;

public interface PrincipalUpdatePolicy {
  Amount apply(Amount prior);
}
