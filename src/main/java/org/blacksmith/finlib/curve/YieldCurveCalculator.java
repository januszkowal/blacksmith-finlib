package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.curve.utils.RateUtils;

public class YieldCurveCalculator {
  public List<YieldCurveRate> values(LocalDate asOfDate, CurveDefinition curveDefinition, List<Knot> knots) {
    ArgChecker.isTrue(knots.size() > 2, "Knots size should be greater than 2");
    var curveFunction = new CurveFunctionFactory().getCurveFunction(curveDefinition.getAlgorithm(), knots);

    int maxX = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var curvePoints = curveFunction.values(0, maxX);
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
