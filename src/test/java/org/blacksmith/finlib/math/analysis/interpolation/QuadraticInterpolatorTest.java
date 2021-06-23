package org.blacksmith.finlib.math.analysis.interpolation;

import org.junit.jupiter.api.Test;

class QuadraticInterpolatorTest {

  @Test
  public void create() {
    double[] xValues = { -1.5, -0.2, 1, 5, 7, 10 };
    double[] yValues = { -1.2, 0, 0.5, 1, 0, 1.2 };
    var function = new QuadraticInterpolator().interpolate(xValues, yValues);

    System.out.println(function.metadata());
//    System.out.println(function.metadata().size());
    System.out.println(function.value(-1.5));
    System.out.println(function.value(-0.19));
    System.out.println(function.value(-0.2));
    System.out.println(function.value(1));
    System.out.println(function.value(9.5));
  }
}
