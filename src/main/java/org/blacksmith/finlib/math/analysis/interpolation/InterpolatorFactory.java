package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.Collection;
import java.util.stream.Stream;

import org.blacksmith.finlib.curve.types.Knot;

public class InterpolatorFactory {
  public InterpolatedFunction createFunction(AlgorithmType curveType, double[] xValues, double[] yValues) {
    InterpolatedFunction curveFunction = null;
    if (curveType == AlgorithmType.AKIMA_SPLINE_BLACKSMITH) {
      curveFunction = new AkimaSplineInterpolator().interpolate(xValues, yValues);
    } else if (curveType == AlgorithmType.LINEAR_BLACKSMITH) {
      curveFunction = new LinearInterpolator().interpolate(xValues, yValues);
    } else if (curveType == AlgorithmType.AKIMA_SPLINE_APACHE_COMMONS) {
      var akimaSplineApacheCommonsFunction =
          new org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator().interpolate(xValues, yValues);
      curveFunction = new InterpolatedFunction() {
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
          new org.apache.commons.math3.analysis.interpolation.LinearInterpolator().interpolate(xValues, yValues);
      curveFunction = new InterpolatedFunction() {
        @Override
        public double value(double x) {
          return linearApacheCommonsInterpolatorFunction.value(x);
        }

        @Override
        public double[] getKnots() {
          return linearApacheCommonsInterpolatorFunction.getKnots();
        }
      };
    } else {
      throw new IllegalArgumentException("Unknown algorithm type: " + curveType);
    }
    return curveFunction;
  }

  public InterpolatedFunction createFunction(AlgorithmType curveType, Collection<Knot> knots) {
    var xValues = knots.stream().mapToDouble(Knot::getX).toArray();
    var yValues = knots.stream().mapToDouble(Knot::getY).toArray();
    return createFunction(curveType, xValues, yValues);
  }

  public InterpolatedFunction createFunction(AlgorithmType curveType, Knot[] knots) {
    var xValues = Stream.of(knots).mapToDouble(Knot::getX).toArray();
    var yValues = Stream.of(knots).mapToDouble(Knot::getY).toArray();
    return createFunction(curveType, xValues, yValues);
  }
}
