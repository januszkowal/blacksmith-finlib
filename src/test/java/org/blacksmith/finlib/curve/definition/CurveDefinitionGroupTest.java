package org.blacksmith.finlib.curve.definition;

import org.blacksmith.finlib.basic.currency.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurveDefinitionGroupTest {

  @Test
  public void createCurveDefinitionGroup() {
    var group = CurveDefinitionGroup.builder()
        .name("zero")
        .curve(Currency.EUR, "zero-eur")
        .curve(Currency.USD, "zero-eur")
        .build();
  }
}
