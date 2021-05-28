package org.blacksmith.finlib.curves;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curves.algoritm.AkimaSplineInterpolator;
import org.blacksmith.finlib.curves.algoritm.LinearInterpolator;
import org.blacksmith.finlib.curves.algoritm.PolynomialFunction;
import org.blacksmith.finlib.curves.types.Knot;
import org.blacksmith.finlib.curves.types.Point2D;

public class CurveFunctionFactory {
  public PolynomialFunction getFunction(CurveType curveType, double[] xvals, double[] yvals) {
    PolynomialFunction curveFunction = null;
    if (curveType == CurveType.AKIMA_SPLINE_BLACKSMITH) {
      curveFunction = new AkimaSplineInterpolator().interpolate(xvals, yvals);
    } else if (curveType == CurveType.LINEAR_BLACKSMITH) {
      curveFunction = new LinearInterpolator().interpolate(xvals, yvals);
    } else if (curveType == CurveType.AKIMA_SPLINE_APACHE_COMMONS) {
      var akimaSplineApacheCommonsFunction =
          new org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator().interpolate(xvals, yvals);
      curveFunction = new PolynomialFunction() {
        @Override
        public double value(double x) {
          return akimaSplineApacheCommonsFunction.value(x);
        }

        @Override
        public double[] getKnots() {
          return akimaSplineApacheCommonsFunction.getKnots();
        }
      };
    } else if (curveType == CurveType.LINEAR_APACHE_COMMONS) {
      var linearApacheCommonsInterpolatorFunction =
          new org.apache.commons.math3.analysis.interpolation.LinearInterpolator().interpolate(xvals, yvals);
      curveFunction = new PolynomialFunction() {
        @Override
        public double value(double x) {
          return linearApacheCommonsInterpolatorFunction.value(x);
        }

        @Override
        public double[] getKnots() {
          return linearApacheCommonsInterpolatorFunction.getKnots();
        }
      };
    }
    return curveFunction;
  }

  public CurveFunction getFunction(CurveType type, List<Knot> knots) {
    List<Point2D> knotsPoints = knots.stream().map(knot -> Point2D.of(knot.getX(), knot.getY()))
        .sorted(Point2D.comparatorByX())
        .collect(Collectors.toList());
    var xvals = knotsPoints.stream().mapToDouble(Point2D::getX).toArray();
    var yvals = knotsPoints.stream().mapToDouble(Point2D::getY).toArray();
    return new YieldCurveFunction(getFunction(type, xvals, yvals));
  }
}
