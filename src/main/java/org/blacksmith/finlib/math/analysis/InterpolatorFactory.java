package org.blacksmith.finlib.math.analysis;

import java.util.Collection;
import java.util.stream.Stream;

import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.interpolation.AkimaSplineInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.DoubleQuadraticInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.math.analysis.interpolation.LinearInterpolator;
import org.blacksmith.finlib.math.analysis.interpolation.QuadraticInterpolator;

public class InterpolatorFactory {
  public InterpolatedFunction createFunction(InterpolationAlgorithm interpolator, double[] xValues, double[] yValues) {
    InterpolatedFunction curveFunction = null;
    if (interpolator == InterpolationAlgorithm.AKIMA_SPLINE) {
      curveFunction = new AkimaSplineInterpolator().interpolate(xValues, yValues);
    } else if (interpolator == InterpolationAlgorithm.LINEAR) {
      curveFunction = new LinearInterpolator().interpolate(xValues, yValues);
    } else if (interpolator == InterpolationAlgorithm.QUADRATIC) {
      curveFunction = new DoubleQuadraticInterpolator().interpolate(xValues, yValues);
    } else if (interpolator == InterpolationAlgorithm.AKIMA_SPLINE_APACHE_COMMONS) {
      var akimaSplineApacheCommonsFunction =
          new org.apache.commons.math3.analysis.interpolation.AkimaSplineInterpolator().interpolate(xValues, yValues);
      curveFunction = new InterpolatedFunction() {
        @Override
        public double value(double x) {
          return akimaSplineApacheCommonsFunction.value(x);
        }

        @Override
        public double[] getXValues() {
          return akimaSplineApacheCommonsFunction.getKnots();
        }
      };
    } else if (interpolator == InterpolationAlgorithm.LINEAR_APACHE_COMMONS) {
      var linearApacheCommonsInterpolatorFunction =
          new org.apache.commons.math3.analysis.interpolation.LinearInterpolator().interpolate(xValues, yValues);
      curveFunction = new InterpolatedFunction() {
        @Override
        public double value(double x) {
          return linearApacheCommonsInterpolatorFunction.value(x);
        }

        @Override
        public double[] getXValues() {
          return linearApacheCommonsInterpolatorFunction.getKnots();
        }
      };
    } else {
      throw new IllegalArgumentException("Unknown algorithm: " + interpolator);
    }
    return curveFunction;
  }

  public InterpolatedFunction createFunction(InterpolationAlgorithm interpolator, Collection<Knot> knots) {
    var xValues = knots.stream().mapToDouble(Knot::getX).toArray();
    var yValues = knots.stream().mapToDouble(Knot::getY).toArray();
    return createFunction(interpolator, xValues, yValues);
  }

  public InterpolatedFunction createFunction(InterpolationAlgorithm interpolator, Knot[] knots) {
    var xValues = Stream.of(knots).mapToDouble(Knot::getX).toArray();
    var yValues = Stream.of(knots).mapToDouble(Knot::getY).toArray();
    return createFunction(interpolator, xValues, yValues);
  }
}
