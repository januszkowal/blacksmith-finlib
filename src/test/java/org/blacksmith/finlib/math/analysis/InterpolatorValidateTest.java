package org.blacksmith.finlib.math.analysis;

import java.util.List;

import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationUtils;
import org.blacksmith.finlib.math.analysis.interpolation.NaturalSplineInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialTestUtils;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class InterpolatorValidateTest {

  private static final double EPS = 1e-14;

  //  @Test
  //  public void shouldAkimaConsecutivePointsYIncrease() {
  //    var knots = create365DayKnots();
  //    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
  //    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
  //    var points = akimaInterpolatorBlackSmith.values(0, maxValue);
  //    assertThat(maxValue).isEqualTo(365);
  //    assertThat(points.size()).isEqualTo(maxValue + 1);
  //    int priorX = -1;
  //    double priorY = 0d;
  //    for (CurvePoint point : points) {
  //      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
  //      assertThat(point.getX()).isEqualTo(priorX + 1);
  //      priorY = point.getY();
  //      priorX = point.getX();
  //    }
  //  }
  //
  //  @Test
  //  public void shouldLinearConsecutivePointsYIncrease() {
  //    var knots = create365DayKnots();
  //    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
  //    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
  //    var points = akimaInterpolatorBlackSmith.values(0, maxValue);
  //    assertThat(maxValue).isEqualTo(365);
  //    assertThat(points.size()).isEqualTo(maxValue + 1);
  //    int priorX = -1;
  //    double priorY = 0d;
  //    for (CurvePoint point : points) {
  //      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
  //      assertThat(point.getX()).isEqualTo(priorX + 1);
  //      priorY = point.getY();
  //      priorX = point.getX();
  //    }
  //  }
  //
    @Test
    public void shouldAkimaGenerateMinSizeCurve() {
      List<Knot> knots = List.of(Knot.of(0, 2.43d), Knot.of(1, 2.50d), Knot.of(7, 3.07d));
      var akimaInterpolatorBlackSmith = new InterpolatorFactory().createFunction(InterpolationAlgorithm.AKIMA_SPLINE, knots);
      var points = akimaInterpolatorBlackSmith.values(0, 7);
      assertThat(points.length).isEqualTo(2);
    }

  //  @Test
  //  public void shouldLinearGenerateMinSizeCurve() {
  //    List<Knot> knots = List.of(Knot.of(0, 2.43d), Knot.of(7, 3.07d));
  //    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
  //    CurveIntegerIterator curveIntegerIterator = new CurveIntegerIterator(LocalDate.now(), 0, 7);
  //    var points = akimaInterpolatorBlackSmith.values(0, 7);
  //    assertThat(points.size()).isEqualTo(8);
  //  }
  private static final double INF = 1. / 0.;
  final InterpolatorFactory factory = new InterpolatorFactory();

  //  /**
  //   *
  //   */
  @Test
  public void recov2ptsTest() {
    final double[] xValues = new double[]{ 1., 2. };
    final double[] yValues = new double[]{ 6., 1. };

    final int nIntervalsExp = 1;
    final int dimExp = 1;
    final double[][] coeffsMatrix = new double[][]{ { 0., 0., 6.0, -5.0 } };

    var interpolator = new NaturalSplineInterpolator().interpolate(xValues, yValues);

    assertThat(interpolator.getIntervals()).isEqualTo(dimExp);
    assertThat(interpolator.getXValues()).containsExactly(xValues);
    PolynomialTestUtils.assertCoeffArray(coeffsMatrix, interpolator);
  }

  @Test
  public void recov4ptsTest() {
    final double[] xValues = new double[]{ 1., 2., 3., 4 };
    final double[] yValues = new double[]{ 6., 25. / 6., 10. / 3., 4. };
    //
    final int nIntervalsExp = 3;
    final double[][] coeffsMatrix =
        new double[][]{ { 6., -2., 0., 1. / 6. },
            { 25. / 6., -3. / 2., 1. / 2., 1. / 6. },
            { 10. / 3. , 0., 1., -1. / 3.} };

    var interpolator = new NaturalSplineInterpolator().interpolate(xValues, yValues);

    assertThat(interpolator.getIntervals()).isEqualTo(nIntervalsExp);
    assertThat(interpolator.getXValues()).containsExactly(xValues);
    PolynomialTestUtils.assertCoeffArray(coeffsMatrix, interpolator);
  }

  private List<Knot> create365DayKnots() {
    return List.of(Knot.of(0, 2.43d),
        Knot.of(1, 2.50d),
        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d));//1Y
  }
}
