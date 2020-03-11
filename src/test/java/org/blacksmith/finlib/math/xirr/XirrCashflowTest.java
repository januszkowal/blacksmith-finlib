package org.blacksmith.finlib.math.xirr;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrCashflowTest {
  @Test
  public void cashflowTest() {
    final XirrCashflow cs = XirrCashflow.of(LocalDate.now(), 10000, 1.0);
    assertEquals(11000.00, cs.presentValue(0.1));
  }
}
