package org.blacksmith.finlib.math.analysis.interpolation;

import org.assertj.core.data.Offset;
import org.blacksmith.finlib.math.analysis.ArrayTestUtils;
import org.blacksmith.finlib.math.analysis.PolynomialTestUtils;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Slf4j
public class AkimaSplineInterpolatorTest {

  private static final double EPS_KNOT = 1e-14;
  private static final double EPS = 1e-6;
  private static final double INF = 1. / 0.;
  final AkimaSplineInterpolator interpolator = new AkimaSplineInterpolator();

  @Test
  public void recov3ptsTest() {
    final double[] X_KNOTS = new double[]{ 1., 2., 3. };
    final double[] Y_KNOTS = new double[]{ 6., 25. / 6., 10. / 3. };

    final int nIntervals = 2;
    final double[][] coeffsMatrix = new double[][]{ { 6., -7. / 3, 0.5, 0 }, { 25. / 6, -4. / 3, 0.5, 0 } };
    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);
    System.out.println(ArrayTestUtils.arrayToFractionString(PolynomialTestUtils.getCoeffMatrix(spline, 4), 1e-9));
    assertThat(spline.getPolynomialCount()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(X_KNOTS);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
  }

  @Test
  public void recov4ptsTest() {
    final double[] X_KNOTS = new double[]{ 1., 2., 3., 4 };
    final double[] Y_KNOTS = new double[]{ 6., 25. / 6, 10. / 3., 4. };
    //
    final int nIntervals = 3;
    final double[][] coeffsMatrix = new double[][]{ { 6., -7. / 3, 3. / 5, -1. / 10 },
        { 25. / 6, -43. / 30, 3. / 5, 0 },
        { 10. / 3, -7. / 30, 21. / 20, -3. / 20 } };

    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);
    //    System.out.println(array2DToFractionString(PolynomialTestUtils.getCoeffMatrix(spline, 4)));

    assertThat(spline.getPolynomialCount()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(X_KNOTS);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
    assertThat(spline.values(X_KNOTS)).containsExactly(Y_KNOTS, Offset.offset(EPS_KNOT));

    double[][] vvv = new double[][]{
        { 1., 6. },
        { 1.2, 20837. / 3750 },
        { 1.5, 1193. / 240 },
        { 1.7, 138791. / 30000 },
        { 2.0, 25. / 6 },
        { 2.3, 2843. / 750 },
        { 2.5, 18. / 5 },
        { 2.7, 2593. / 750 },
        { 2.9, 1261. / 375 },
        { 3.0, 10. / 3 }
    };

    double[] xValues1 = ArrayTestUtils.getCol(vvv, 0);
    double[] yValues1 = ArrayTestUtils.getCol(vvv, 1);
    //    System.out.println(ArrayTestUtils.getArrayFractions(spline.values(xValues1), 1e-9));
    assertThat(spline.values(xValues1)).containsExactly(yValues1, Offset.offset(EPS));
  }

  @Test
  public void shortDataLengthTest() {
    double[] X_KNOTS1 = new double[]{ 1. };
    double[] Y_KNOTS1 = new double[]{ 4. };
    double[] X_KNOTS2 = new double[]{ 1., 2. };
    double[] Y_KNOTS2 = new double[]{ 4., 4. };
    double[] X_KNOTS3 = new double[]{ 1., 2., 3. };
    double[] Y_KNOTS3 = new double[]{ 4., 4., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS1, Y_KNOTS1));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS2, Y_KNOTS2));

    interpolator.interpolate(X_KNOTS3, Y_KNOTS3);
  }
}
