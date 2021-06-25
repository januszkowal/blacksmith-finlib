package org.blacksmith.finlib.math.analysis;

import org.blacksmith.commons.arrays.ArrayUtils;
import org.blacksmith.finlib.math.analysis.interpolation.DoubleQuadraticFunction;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialSplineFunction;

public class PolynomialTestUtils {

  public static double[][] getCoeffMatrix(PolynomialSplineFunction interpolator, int rowSize) {
    double[][] result = new double[interpolator.getPolynomialCount()][];
    for (int i = 0; i < interpolator.getPolynomialCount(); ++i) {
      result[i] = ArrayUtils.leftPad(interpolator.getPolynomialCoefficients(i), rowSize);
    }
    return result;
  }

  public static double[][] getCoeffMatrix(DoubleQuadraticFunction interpolator, int rowSize) {
    double[][] result = new double[interpolator.getPolynomialCount()][];
    for (int i = 0; i < interpolator.getPolynomialCount(); ++i) {
      result[i] = ArrayUtils.leftPad(interpolator.getPolynomialCoefficients(i), rowSize);
    }
    return result;
  }
}
