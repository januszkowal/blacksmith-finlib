package org.blacksmith.finlib.rounding;

import org.junit.jupiter.api.Test;

public class SimpleRounding {
  @Test
  public void roundingUp() {
    Rounding rounding = RoundingFactory.of(RoundingMode.UP, 1, 4);
    System.out.println("rounding " + rounding.round(123.36d));
    rounding = RoundingFactory.of(RoundingMode.UP, -3, 8);
    System.out.println("rounding " + rounding.round(5123.623d));
    System.out.println("rounding " + rounding.round(5123.624d));
    System.out.println("rounding " + rounding.round(5123.625d));
    System.out.println("rounding " + rounding.round(5123.626d));
    System.out.println("rounding " + rounding.round(5123.726d));
    System.out.println("rounding " + rounding.round(5212.4826d));
  }
}
