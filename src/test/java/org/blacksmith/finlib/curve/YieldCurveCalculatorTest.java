package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class YieldCurveCalculatorTest {
  final LocalDate asOfDate = LocalDate.now();
  final YieldCurveCalculator calculator = new YieldCurveCalculator();

  @Test
  public void shouldGenerateWholeCurve() {
    CurveDefinition definition = CurveDefinition.of("BONDS", AlgorithmType.AKIMA_SPLINE_BLACKSMITH, 365);
    List<Knot> knots = create365DayKnots();
    int knotsMin = knots.stream().mapToInt(Knot::getX).min().getAsInt();
    int knotsMax = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var curveRates = calculator.values(asOfDate, definition, knots);
    assertThat(curveRates.size()).isEqualTo(knotsMax - knotsMin + 1);
  }

  @Test
  public void shouldGeneratePart() {
    CurveDefinition definition = CurveDefinition.of("BONDS", AlgorithmType.AKIMA_SPLINE_BLACKSMITH, 365);
    List<Knot> knots = create365DayKnots();
    var curveRates = calculator.values(asOfDate, definition, knots, 5, 104);
    assertThat(curveRates.size()).isEqualTo(100);
  }

  @Test
  public void shouldGenerateFail() {
    CurveDefinition definition = CurveDefinition.of("BONDS", AlgorithmType.AKIMA_SPLINE_BLACKSMITH, 365);
    List<Knot> knots = create365DayKnots();
    int knotsMin = knots.stream().mapToInt(Knot::getX).min().getAsInt();
    int knotsMax = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var curveRates = calculator.values(asOfDate, definition, knots, knotsMin, knotsMax);
    assertThrows(IllegalArgumentException.class, () -> calculator.values(asOfDate, definition, knots, knotsMin - 1, 10));
    assertThrows(IllegalArgumentException.class, () -> calculator.values(asOfDate, definition, knots, 0, knotsMax + 1));
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

