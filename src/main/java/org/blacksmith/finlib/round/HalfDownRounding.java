package org.blacksmith.finlib.round;

import java.math.BigDecimal;

public class HalfDownRounding implements Rounding {
  @Override public BigDecimal round(BigDecimal value) {
    return BigDecimal.ZERO;
  }
}
