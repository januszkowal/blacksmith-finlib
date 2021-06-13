package org.blacksmith.finlib.curve;

import java.time.LocalDate;

import org.blacksmith.finlib.curve.iterator.CurveDateIterator;
import org.blacksmith.finlib.curve.iterator.CurveIntegerIterator;
import org.blacksmith.finlib.curve.iterator.CurveIterator;
import org.blacksmith.finlib.interest.basis.StandardDayCounts;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

class CurveIteratorTest {
  @Test
  public void shouldIntegersIerate() {
    Curve curve = Mockito.mock(Curve.class);
    Mockito.when(curve.getDayCount()).thenReturn(StandardDayCounts.ACT_360);
    LocalDate valuationDate = LocalDate.of(2021, 7, 1);
    CurveIterator iterator = CurveIntegerIterator.of(valuationDate, 5, 8, curve);
    var values = iterator.values();
    assertThat(values.size()).isEqualTo(4);
  }

  @Test
  public void shouldDatesIterate2() {
    Curve curve = Mockito.mock(Curve.class);
    Mockito.when(curve.getDayCount()).thenReturn(StandardDayCounts.ACT_360);
    LocalDate valuationDate = LocalDate.of(2021, 7, 1);
    LocalDate d1 = LocalDate.of(2021, 7, 20);
    LocalDate d2 = LocalDate.of(2021, 7, 25);
    CurveIterator iterator = CurveDateIterator.of(valuationDate, d1, d2, curve);
    var values = iterator.values();
    assertThat(values.size()).isEqualTo(6);
  }
}
