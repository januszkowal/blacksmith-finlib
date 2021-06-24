package org.blacksmith.finlib.math.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;
import org.blacksmith.commons.arrays.ArrayUtils;
import org.blacksmith.finlib.math.analysis.interpolation.PolynomialSplineFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class ArrayTestUtils {

  public static void assertArray(double[][] actual, double[][] expected, double epsilon) {
    assertThat(expected.length).isEqualTo(actual.length);
    for (int i = 0; i < expected.length; ++i) {
      System.out.println("Row: " + i);
      assertThat(actual[i]).containsExactly(expected[i], offset(epsilon));
    }
  }

  public static void assertArrayWithNan(double[] expected, double[] actual, double epsilon) {
    for (int i = 0; i < actual.length; i++) {
      double ref;
      if (Double.isNaN(expected[i])) {
        ref = epsilon;
      } else {
        ref = expected[i] == 0. ? 1. : Math.abs(expected[i]);
      }
      assertThat(actual[i]).isCloseTo(expected[i], offset(ref * epsilon))
          .describedAs("Cell: " + i);
    }
  }

  public static double[] getCol(double[][] array, int col) {
    return Arrays.stream(array).mapToDouble(row -> row[col]).toArray();
  }

  public static String arrayToString(double[] array) {
    return Arrays.stream(array).mapToObj(x -> Double.valueOf(x).toString())
        .collect(Collectors.joining(", "));
  }

  public static String arrayToString(double[][] array) {
    return Arrays.stream(array).map(row -> Arrays.stream(row).mapToObj(r -> Double.valueOf(r).toString())
        .collect(Collectors.joining(",","[","]"))).collect(
        Collectors.joining("\n","",""));
  }

  public static String arrayToFractionString(double[][] array, double epsilon) {
    return Arrays.stream(array).map(row -> Arrays.stream(row).mapToObj(r ->new Fraction(r, epsilon, 1000000).toString())
        .collect(Collectors.joining(", ","[","]"))).collect(
        Collectors.joining("\n","",""));
  }

  public static List<String> arrayToFractionString(double[] values, double epsilon) {
    return Arrays.stream(values).mapToObj(x -> new Fraction(x, epsilon, 1000000).toString())
        .collect(Collectors.toList());
  }
}
