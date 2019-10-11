package org.blacksmith.finlib.round;

import java.math.BigDecimal;

public interface Rounding {
  default double round(double value) {
    return round(BigDecimal.valueOf(value)).doubleValue();
  }

  BigDecimal round(BigDecimal value);
}
