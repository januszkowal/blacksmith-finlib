package org.blacksmith.finlib.rates.fxrates.impl;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FxRate3InternalTest {
  @Test
  public void shouldMultiply() {
    var rate1 = FxRate3Internal.of(LocalDate.now(), 3.1d, 4.9d, 4d, 6);
    rate1.multiply(3d);
    var fxRate1 = rate1.toFxRate3();
    assertThat(fxRate1.getValue())
        .extracting(r -> r.getBuy().doubleValue(), r -> r.getSell().doubleValue(), r -> r.getAvg().doubleValue())
        .containsExactly(9.3d, 14.7d, 12d);
  }
}
