package org.blacksmith.finlib.math.analysis.interpolation;

import org.blacksmith.finlib.math.analysis.ArrayTestUtils;
import org.blacksmith.finlib.math.analysis.PolynomialTestUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

public class DoubleQuadraticInterpolatorTest {
  private static final double EPS = 1e-7;
  @Test
  public void nodesValueTest() {
    final double[] X_KNOTS = {0.0, 0.4, 1.0, 1.8, 2.8, 5.0};
    final double[] Y_KNOTS = {3.0, 4.0, 3.1, 2.0, 7.0, 2.0};
    final double[] X_TEST = {-0.0, 0.0, 0.3, 1.0, 2.0, 4.5, 5.0};
    final double[] Y_TEST = new double[] {3.0, 3.0, 3.87, 3.1, 2.619393939, 5.068181818, 2.0};
    var spline = new DoubleQuadraticInterpolator().interpolate(X_KNOTS, Y_KNOTS);
    System.out.println(ArrayTestUtils.arrayToString(PolynomialTestUtils.getCoeffMatrix(spline, 3)));
//    PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
//    row=RealPolynomialFunction1D[_coefficients=[4.0, 0.8999999999999998, -4.0], _n=3]
//    row=RealPolynomialFunction1D[_coefficients=[3.1, -1.4464285714285714, 0.08928571428571429], _n=3]
//    row=RealPolynomialFunction1D[_coefficients=[2.0, 1.458333333333334, 3.5416666666666674], _n=3]
//    row=RealPolynomialFunction1D[_coefficients=[7.0, 2.727272727272728, -2.272727272727273], _n=3]
    assertThat(spline.values(X_TEST)).containsExactly(Y_TEST, offset(EPS));
//    assertThat(spline.value(X_TEST[0])).isEqualTo(Y_TEST[0], offset(EPS));
//    assertThat(spline.value(X_TEST[1])).isEqualTo(Y_TEST[1], offset(EPS));
//    assertThat(spline.value(X_TEST[2])).isEqualTo(Y_TEST[2], offset(EPS));
//    assertThat(spline.value(X_TEST[3])).isEqualTo(Y_TEST[3], offset(EPS));
//    assertThat(spline.value(X_TEST[4])).isEqualTo(Y_TEST[4], offset(EPS));
//    assertThat(spline.value(X_TEST[5])).isEqualTo(Y_TEST[5], offset(EPS));
  }
}
