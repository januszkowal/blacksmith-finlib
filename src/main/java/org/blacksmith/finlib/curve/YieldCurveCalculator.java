package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;

public class YieldCurveCalculator {
  public List<YieldCurveRate> values(LocalDate valuationDate, CurveDefinition curveDefinition, List<Knot> knots) {
    ArgChecker.isTrue(knots.size() > 2, "Knots size should be greater than 2");
//    int min = knots.stream().mapToInt(Knot::getX).min().getAsInt();
//    int max = knots.stream().mapToInt(Knot::getX).max().getAsInt();
//    return getCurveFunction(curveDefinition, knots).values(min, max).stream()
//        .map(curvePoint -> pointToRate(valuationDate, 365, curvePoint))
//        .collect(Collectors.toList());
    return null;
  }

  public List<YieldCurveRate> values(LocalDate valuationDate, CurveDefinition curveDefinition, List<Knot> knots,
      int min, int max) {
    ArgChecker.isTrue(knots.size() > 2, "Knots size should be greater than 2");
//    int knotsMin = knots.stream().mapToInt(Knot::getX).min().getAsInt();
//    int knotsMax = knots.stream().mapToInt(Knot::getX).max().getAsInt();
//    ArgChecker.isTrue(min >= knotsMin, MessageFormat.format("Minimum X must be greater or equal: {0}", knotsMin));
//    ArgChecker.isTrue(max <= knotsMax, MessageFormat.format("Maximum X must be lower or equal: {0}", knotsMax));
//    return getCurveFunction(curveDefinition, knots).values(min, max).stream()
//        .map(curvePoint -> pointToRate(valuationDate, 365, curvePoint))
//        .collect(Collectors.toList());
  return null;
  }

  private Curve getCurveFunction(CurveDefinition curveDefinition, List<Knot> knots) {
//    return new InterpolatorFactory().getCurveFunction(curveDefinition.getAlgorithm(), knots);
    return null;
  }

  private YieldCurveRate pointToRate(LocalDate asOfDate, int yearLength, CurvePoint point) {
//    LocalDate pointDate = asOfDate.plusDays(point.getX());
//    var dcf = RateUtils.interestRate100ToDcfContDisc(asOfDate, pointDate, point.getY(), yearLength);
//    return YieldCurveRate.of(pointDate, point.isKnot(), point.getY(), dcf);
    return null;
  }
}
