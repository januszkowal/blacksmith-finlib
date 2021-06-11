package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;
import org.blacksmith.finlib.curve.node.SimpleCurveNodeReferenceData;
import org.blacksmith.finlib.interest.basis.StandardDayCounts;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CurveFactoryTest {
  CurveFactory curveFactory = new CurveFactory();

  @Test
  public void shouldReturnKnots() {
    LocalDate valuationDate = LocalDate.of(2021, 5, 15);
    CurveDefinition definition = CurveDefinition.builder()
        .interpolator(InterpolationAlgorithm.AKIMA_SPLINE_BLACKSMITH)
        .dayCount(StandardDayCounts.ACT_360)
        .curveNodes(List.of())
        .curveName("ZERO")
        .build();
    List<CurveNodeReferenceData> referenceNodes = List.of(
        SimpleCurveNodeReferenceData.of("1D", Tenor.TENOR_1D, 2d ),
        SimpleCurveNodeReferenceData.of("1W", Tenor.TENOR_1W, 2.2d),
        SimpleCurveNodeReferenceData.of("2W", Tenor.TENOR_2W, 2.3d),
        SimpleCurveNodeReferenceData.of("1M", Tenor.TENOR_1M, 2.5d),
        SimpleCurveNodeReferenceData.of("3M", Tenor.TENOR_3M, 2.7d),
        SimpleCurveNodeReferenceData.of("6M", Tenor.TENOR_6M, 2.9d),
        SimpleCurveNodeReferenceData.of("1Y", Tenor.TENOR_1Y, 3.3d),
        SimpleCurveNodeReferenceData.of("3Y", Tenor.TENOR_3Y, 3.6d),
        SimpleCurveNodeReferenceData.of("5Y", Tenor.TENOR_5Y, 3.8d));
    var curve = curveFactory.createCurve(valuationDate, definition, referenceNodes);

    assertThat(curve.value(valuationDate.minusDays(10))).isEqualTo(2d);
    assertThat(curve.value(valuationDate)).isEqualTo(2d);
    assertThat(curve.value(valuationDate.plusDays(7))).isEqualTo(2.2d);
    assertThat(curve.value(valuationDate.plusDays(14))).isEqualTo(2.3d);
    assertThat(curve.value(valuationDate.plusMonths(1))).isEqualTo(2.5d);
    assertThat(curve.value(valuationDate.plusMonths(3))).isEqualTo(2.7d);
    assertThat(curve.value(valuationDate.plusMonths(6))).isEqualTo(2.9d);
    assertThat(curve.value(valuationDate.plusYears(1))).isEqualTo(3.3d);
    assertThat(curve.value(valuationDate.plusYears(3))).isEqualTo(3.6d);
    assertThat(curve.value(valuationDate.plusYears(5))).isEqualTo(3.8d);
    assertThat(curve.value(valuationDate.plusYears(6))).isEqualTo(3.8d);
  }


}
