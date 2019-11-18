package org.blacksmith.finlib.xirr;

import org.blacksmith.finlib.xirr.XirrCashflow;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrCashflowTest {
  @Test
  public void cashflowTest() {
    final XirrCashflow cs = new XirrCashflow(10000, 1.0);
    assertEquals(11000.00, cs.presentValue(0.1));
  }
}
