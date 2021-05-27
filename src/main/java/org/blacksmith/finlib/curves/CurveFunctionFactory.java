package org.blacksmith.finlib.curves;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curves.algoritm.AkimaSplineInterpolator;
import org.blacksmith.finlib.curves.algoritm.LinearInterpolator;
import org.blacksmith.finlib.curves.algoritm.PolynomialFunction;
import org.blacksmith.finlib.curves.types.Knot;
import org.blacksmith.finlib.curves.types.Point2D;

public class CurveFunctionFactory {
  public PolynomialFunction getFunction(String name, double[] xvals, double[] yvals) {
    PolynomialFunction curveFunction = null;
    if (name.equals("AkimaSplineBlacksmith")) {
      var akimaBlachsmithInterpolator = new AkimaSplineInterpolator();
      curveFunction = akimaBlachsmithInterpolator.interpolate(xvals, yvals);
    } else if (name.equals("LinearBlacksmith")) {
      var linearBlacksmithInterpolator = new LinearInterpolator();
      curveFunction = linearBlacksmithInterpolator.interpolate(xvals, yvals);
    } else if (name.equals("AkimaSplineApacheCommons")) {
      var akimaSplineApacheCommonsInterpolator = new org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator();
      var akimaSplineApacheCommonsFunction = akimaSplineApacheCommonsInterpolator.interpolate(xvals, yvals);
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
    } else if (name.equals("LinearApacheCommons")) {
      var linearApacheCommonsInterpolator = new org.apache.commons.math3.analysis.interpolation.LinearInterpolator();
      var linearApacheCommonsInterpolatorFunction = linearApacheCommonsInterpolator.interpolate(xvals, yvals);
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
  public CurveFunction getFunction(String name, List<Knot> knots) {
    List<Point2D> knotsPoints = knots.stream().map(knot -> Point2D.of(knot.getX(), knot.getY()))
        .sorted(Point2D.comparatorByX())
        .collect(Collectors.toList());
    var xvals = knotsPoints.stream().mapToDouble(Point2D::getX).toArray();
    var yvals = knotsPoints.stream().mapToDouble(Point2D::getY).toArray();
    return new YieldCurveFunction(getFunction(name, xvals, yvals));
  }
}
