package org.blacksmith.finlib.curve;

import java.util.ArrayList;
import java.util.List;

import org.blacksmith.finlib.curve.algoritm.AlgorithmType;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CurveValidationTest {

  CurveFunctionFactory factory = new CurveFunctionFactory();

  @Test
  public void shouldAkimaConsecutivePointsYIncrease() {
    var knots = create365DayKnots();
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var akimaInterpolatorBlackSmith = factory.getFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, maxValue);
    assertThat(maxValue).isEqualTo(365);
    assertThat(points.size()).isEqualTo(maxValue+1);
    int priorX =-1;
    double priorY = 0d;
    for (CurvePoint point: points) {
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
    var akimaInterpolatorBlackSmith = factory.getFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, maxValue);
    assertThat(maxValue).isEqualTo(365);
    assertThat(points.size()).isEqualTo(maxValue+1);
    int priorX =-1;
    double priorY = 0d;
    for (CurvePoint point: points) {
      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
      assertThat(point.getX()).isEqualTo(priorX + 1);
      priorY = point.getY();
      priorX = point.getX();
    }
  }

  @Test
  public void shouldAkimaGenerateMinSizeCurve() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));
    knots.add(Knot.of(1, 2.50d));
    knots.add(Knot.of(7, 3.07d));
    var akimaInterpolatorBlackSmith = factory.getFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, 7);
    assertThat(points.size()).isEqualTo(8);
  }

  @Test
  public void shouldLinearGenerateMinSizeCurve() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));
    knots.add(Knot.of(7, 3.07d));
    var akimaInterpolatorBlackSmith = factory.getFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, 7);
    assertThat(points.size()).isEqualTo(8);
  }


  private List<Knot> create365DayKnots() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));//1D
    knots.add(Knot.of(1, 2.50d));//1D
    knots.add(Knot.of(7, 3.07d));//1D
    knots.add(Knot.of(14, 3.36d));//1W
    knots.add(Knot.of(30, 3.71d));//2W
    knots.add(Knot.of(90, 4.27d));//1M
    knots.add(Knot.of(182, 4.38d));//6M
    knots.add(Knot.of(273, 4.47d));//6M
    knots.add(Knot.of(365, 4.52d));//1Y
    return knots;
  }
}
