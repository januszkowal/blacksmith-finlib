package org.blacksmith.finlib.curves;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.blacksmith.finlib.curves.algoritm.AkimaSplineInterpolator;
import org.blacksmith.finlib.curves.algoritm.LinearInterpolator;
import org.blacksmith.finlib.curves.algoritm.SingleArgumentFunction;
import org.blacksmith.finlib.curves.types.Knot;
import org.blacksmith.finlib.curves.types.Point2D;

public class CurveInterpolatorFactory {
  public YieldCurveFunction getInterpolator(String name, List<Knot> knots) {
    SingleArgumentFunction curveFunction = null;
    List<Point2D> knotsPoints = knots.stream().map(knot -> Point2D.of(knot.getX(), knot.getY()))
        .sorted(Point2D.comparatorByX())
        .collect(Collectors.toList());
    var knotsX = knotsPoints.stream().mapToDouble(Point2D::getX).toArray();
    var knotsY = knotsPoints.stream().mapToDouble(Point2D::getY).toArray();
    if (name.equals("AkimaSplineBlackSmith")) {
      AkimaSplineInterpolator akimaInterpolator = new AkimaSplineInterpolator();
      var akimaSplineFunctionBS = akimaInterpolator.interpolate(knotsX, knotsY);
      curveFunction = x -> akimaSplineFunctionBS.value(x);
    } else if (name.equals("LinearBlackSmith")) {
      var linearInterpolator = new LinearInterpolator();
      var linearFunction = linearInterpolator.interpolate(knotsX, knotsY);
      curveFunction = x -> linearFunction.value(x);
    } else if (name.equals("AkimaSplineApacheCommons")) {
      org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator
          akimaSplineInterpolator = new org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator();
      PolynomialSplineFunction polynominal = akimaSplineInterpolator.interpolate(knotsX, knotsY);
      curveFunction = x -> polynominal.value(x);
    } else if (name.equals("LinearApacheCommons")) {
      org.apache.commons.math3.analysis.interpolation.LinearInterpolator linearInterpolator =
          new org.apache.commons.math3.analysis.interpolation.LinearInterpolator();
      PolynomialSplineFunction polynominal = linearInterpolator.interpolate(knotsX, knotsY);
      curveFunction = x -> polynominal.value(x);
    }
    return new YieldCurveFunction(knots, curveFunction);
  }
}
