package org.blacksmith.finlib.curve;

import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curve.algorithm.AkimaSplineInterpolator;
import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.algorithm.LinearInterpolator;
import org.blacksmith.finlib.curve.algorithm.PolynomialFunction;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.curve.types.Point2D;

public class CurveFunctionFactory {
  public PolynomialFunction getFunction(AlgorithmType curveType, double[] xvals, double[] yvals) {
    PolynomialFunction curveFunction = null;
    if (curveType == AlgorithmType.AKIMA_SPLINE_BLACKSMITH) {
      curveFunction = new AkimaSplineInterpolator().interpolate(xvals, yvals);
    } else if (curveType == AlgorithmType.LINEAR_BLACKSMITH) {
      curveFunction = new LinearInterpolator().interpolate(xvals, yvals);
    } else if (curveType == AlgorithmType.AKIMA_SPLINE_APACHE_COMMONS) {
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
    } else if (curveType == AlgorithmType.LINEAR_APACHE_COMMONS) {
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

  public CurveFunction getFunction(AlgorithmType type, List<Knot> knots) {
    List<Point2D> knotsPoints = knots.stream()
        .sorted()
        .map(knot -> Point2D.of(knot.getX(), knot.getY()))
        .collect(Collectors.toList());
    var xvals = knotsPoints.stream().mapToDouble(Point2D::getX).toArray();
    var yvals = knotsPoints.stream().mapToDouble(Point2D::getY).toArray();
    return new CurveFunctionImpl(getFunction(type, xvals, yvals));
  }
}
