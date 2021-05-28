package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.curve.utils.RateUtils;

public class YieldCurveCalculator {
  public List<YieldCurveRate> calculate(LocalDate asOfDate, CurveDefinition curveDefinition, List<Knot> knots) {
    var curveFunction = new CurveFunctionFactory().getFunction(curveDefinition.getAlgorithm(), knots);

    int maxx = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var curvePoints = curveFunction.curveValues(0, maxx);
    return curvePoints.stream()
        .map(curvePoint -> pointToRate(asOfDate, curveDefinition.getYearLength(), curvePoint))
        .collect(Collectors.toList());
  }

  private YieldCurveRate pointToRate(LocalDate asOfDate, int yearLength, CurvePoint point) {
    LocalDate pointDate = asOfDate.plusDays(point.getX());
    var dcf = RateUtils.interestRate100ToDcf(asOfDate, pointDate, point.getY(), yearLength);
    return YieldCurveRate.of(pointDate, point.isKnot(), point.getY(), dcf);
  }
}