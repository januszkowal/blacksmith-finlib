package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YieldCurveCalculatorTest {
  LocalDate asOfDate = LocalDate.now();
  YieldCurveCalculator calculator = new YieldCurveCalculator();

  @Test
  public void testGenerate() {
    CurveDefinition definition = CurveDefinition.of("BONDS", AlgorithmType.AKIMA_SPLINE_BLACKSMITH, 365);
    List<Knot> knots = create365DayKnots();
    var curveRates = calculator.values(asOfDate, definition, knots);
    curveRates.forEach(rate -> log.info("{}", rate));
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

