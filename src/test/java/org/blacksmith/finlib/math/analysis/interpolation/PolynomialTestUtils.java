package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class PolynomialTestUtils {
  private static final double EPS = 1e-14;

  public static void assertCoeffArray(double[][] expected, PolynomialSplineFunction interpolator) {
    for (int i = 0; i < expected.length; ++i) {
      var coeff = InterpolationUtils.arrayPadLeft(interpolator.getPolynomialCoefficients(i), expected[i].length);
      //      System.out.println("Row expected " + i + ": " + arrayToString(expected[i]));
      //      System.out.println("Row actual   " + i + ": " + arrayToString(coeff));
      //      System.out.println("Row aaaaaa   " + i + ": " + arrayToString(interpolator.getPolynomialCoefficients(i)));
      for (int j = 0; j < coeff.length; j++) {
        final double ref = expected[i][j] == 0. ? 1. : Math.abs(expected[i][j]);
        System.out.println("Cell: " + i + "/" + j);
        assertThat(coeff[j]).isCloseTo(expected[i][j], offset(EPS))
            .describedAs("Cell: " + i + "/" + j);
      }
    }
  }

  public static void assertArray(double[] expected, double[] actual) {
    for (int i = 0; i < actual.length; i++) {
      double ref;
      if (Double.isNaN(expected[i])) {
        ref = EPS;
      }
      else {
        ref = expected[i] == 0. ? 1. : Math.abs(expected[i]);
      }
      assertThat(actual[i]).isCloseTo(expected[i], offset(ref * EPS))
          .describedAs("Cell: " + i);
    }
  }

  public static String arrayToString(double[] array) {
    return Arrays.stream(array).mapToObj(x -> Double.valueOf(x).toString())
        .collect(Collectors.joining(", "));
  }

  public static double[] getCol(double[][] array, int col) {
    return Arrays.stream(array).mapToDouble(row -> row[col]).toArray();
  }
}
