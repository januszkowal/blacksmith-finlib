package org.blacksmith.finlib.rounding;

import java.math.BigDecimal;

public interface Rounding {
  
  default double round(double value) {
    // conversion to string is required to avoid inexact values
    /* It is important to use one of the following, that guarantees translation double->string-BigDecimal:
     * - BigDecimal.valueOf(double value)
     * - new BigDecimal(Double.toString(double value))
     * */
    return round(BigDecimal.valueOf(value)).doubleValue();
  }

  BigDecimal round(BigDecimal value);
  
}
