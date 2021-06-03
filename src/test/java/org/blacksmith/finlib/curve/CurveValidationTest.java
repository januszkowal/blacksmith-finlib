package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class CurveValidationTest {

  final CurveFunctionFactory factory = new CurveFunctionFactory();

  @Test
  public void shouldAkimaConsecutivePointsYIncrease() {
    var knots = create365DayKnots();
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var akimaInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.values(0, maxValue);
    assertThat(maxValue).isEqualTo(365);
    assertThat(points.size()).isEqualTo(maxValue + 1);
    int priorX = -1;
    double priorY = 0d;
    for (CurvePoint point : points) {
      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
      assertThat(point.getX()).isEqualTo(priorX + 1);
      priorY = point.getY();
      priorX = point.getX();
    }
  }

  @Test
  public void shouldLinearConsecutivePointsYIncrease() {
    var knots = create365DayKnots();
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var akimaInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.values(0, maxValue);
    assertThat(maxValue).isEqualTo(365);
    assertThat(points.size()).isEqualTo(maxValue + 1);
    int priorX = -1;
    double priorY = 0d;
    for (CurvePoint point : points) {
      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
      assertThat(point.getX()).isEqualTo(priorX + 1);
      priorY = point.getY();
      priorX = point.getX();
    }
  }

  @Test
  public void shouldAkimaGenerateMinSizeCurve() {
    List<Knot> knots = List.of(Knot.of(0, 2.43d), Knot.of(1, 2.50d), Knot.of(7, 3.07d));
    var akimaInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.values(0, 7);
    assertThat(points.size()).isEqualTo(8);
  }

  @Test
  public void shouldLinearGenerateMinSizeCurve() {
    List<Knot> knots = List.of(Knot.of(0, 2.43d), Knot.of(7, 3.07d));
    var akimaInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.values(0, 7);
    assertThat(points.size()).isEqualTo(8);
  }

  @Test
  public void shouldIncreaseFail() {
    List<Knot> knots = List.of(Knot.of(0, 0),
        Knot.of(1, 2),
        Knot.of(5, 2.2),
        Knot.of(5, 2.3),
        Knot.of(10, 2.5),
        Knot.of(15, 2.8));
    assertThrows(IllegalArgumentException.class, () -> factory.getCurveFunction(AlgorithmType.LINEAR_BLACKSMITH, knots));
    assertThrows(IllegalArgumentException.class, () -> factory.getCurveFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots));
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
