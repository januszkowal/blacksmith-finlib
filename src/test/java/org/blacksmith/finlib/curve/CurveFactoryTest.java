package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.curve.discount.CurveDiscountFactor;
import org.blacksmith.finlib.curve.discount.ZeroRateDiscountFactor;
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
        SimpleCurveNodeReferenceData.of("1D", Tenor.TENOR_1D, 0.02d ),
        SimpleCurveNodeReferenceData.of("1W", Tenor.TENOR_1W, 0.022d),
        SimpleCurveNodeReferenceData.of("2W", Tenor.TENOR_2W, 0.023d),
        SimpleCurveNodeReferenceData.of("1M", Tenor.TENOR_1M, 0.025d),
        SimpleCurveNodeReferenceData.of("3M", Tenor.TENOR_3M, 0.027d),
        SimpleCurveNodeReferenceData.of("6M", Tenor.TENOR_6M, 0.029d),
        SimpleCurveNodeReferenceData.of("1Y", Tenor.TENOR_1Y, 0.033d),
        SimpleCurveNodeReferenceData.of("3Y", Tenor.TENOR_3Y, 0.036d),
        SimpleCurveNodeReferenceData.of("5Y", Tenor.TENOR_5Y, 0.038d));
    var curve = curveFactory.createCurve(valuationDate, definition, referenceNodes);
    CurveDiscountFactor df = CurveDiscountFactor.of(curve, ZeroRateDiscountFactor.of());

    assertThat(curve.value(valuationDate.minusDays(10))).isEqualTo(0.02d);
    assertThat(curve.value(valuationDate)).isEqualTo(0.02d);
    assertThat(curve.value(valuationDate.plusDays(7))).isEqualTo(0.022d);
    assertThat(curve.value(valuationDate.plusDays(14))).isEqualTo(0.023d);
    assertThat(curve.value(valuationDate.plusMonths(1))).isEqualTo(0.025d);
    assertThat(curve.value(valuationDate.plusMonths(3))).isEqualTo(0.027d);
    assertThat(curve.value(valuationDate.plusMonths(6))).isEqualTo(0.029d);
    assertThat(curve.value(valuationDate.plusYears(1))).isEqualTo(0.033d);
    assertThat(curve.value(valuationDate.plusYears(3))).isEqualTo(0.036d);
    assertThat(curve.value(valuationDate.plusYears(5))).isEqualTo(0.038d);
    assertThat(curve.value(valuationDate.plusYears(6))).isEqualTo(0.038d);

    assertThat(df.discountFactor(valuationDate)).isEqualTo(1d);
    assertThat(df.discountFactor(valuationDate.plusYears(1))).isEqualTo(0.9670952060255066d);
  }


}
