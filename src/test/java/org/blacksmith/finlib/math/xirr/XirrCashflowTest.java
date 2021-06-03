package org.blacksmith.finlib.math.xirr;

import org.blacksmith.finlib.math.xirr.dto.XirrCashflow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XirrCashflowTest {
  @Test
  public void cashflowTest() {
    final XirrCashflow cs = XirrCashflow.of(10000, 1.0);
    assertEquals(11000.00, cs.futureValue(0.1));
    assertEquals(10000.00, cs.derivativeValue(0.1));
  }
}
