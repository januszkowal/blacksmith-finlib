package org.blacksmith.finlib.math.analysis.interpolation;

import org.assertj.core.data.Offset;
import org.blacksmith.finlib.math.analysis.ArrayTestUtils;
import org.blacksmith.finlib.math.analysis.PolynomialTestUtils;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Slf4j
public class NaturalSplineInterpolatorTest {

  private static final double EPS_KNOT = 1e-14;
  private static final double EPS = 1e-6;
  private static final double INF = 1. / 0.;
  final NaturalSplineInterpolator interpolator = new NaturalSplineInterpolator();

  @Test
  public void recov2ptsTest() {
    final double[] X_KNOTS = new double[]{ 1., 2. };
    final double[] Y_KNOTS = new double[]{ 6., 1. };

    final int nIntervals = 1;
    final double[][] coeffsMatrix = new double[][]{ { 0., 0., 6.0, -5.0 } };

    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);

    assertThat(spline.getPolynomialCount()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(X_KNOTS);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
  }

  @Test
  public void recov4ptsTest() {
    final double[] X_KNOTS = new double[]{ 1., 2., 3., 4 };
    final double[] Y_KNOTS = new double[]{ 6., 25. / 6., 10. / 3., 4. };
    //
    final int nIntervals = 3;
    final double[][] coeffsMatrix =
        new double[][]{ { 6., -2., 0., 1. / 6. },
            { 25. / 6., -3. / 2., 1. / 2., 1. / 6. },
            { 10. / 3., 0., 1., -1. / 3. } };

    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);

    assertThat(spline.getPolynomialCount()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(X_KNOTS);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
    assertThat(spline.values(X_KNOTS)).containsExactly(Y_KNOTS, Offset.offset(EPS_KNOT));

    double[][] vvv = new double[][]{
        { 1., 6. },
        { 1.2, 4201. / 750 },
        { 1.5, 241. / 48 },
        { 1.7, 5556. / 1193. },
        { 2.0, 25. / 6 },
        { 2.3, 22597. / 6000 },
        { 2.5, 57. / 16 },
        { 2.7, 20513. / 6000 },
        { 2.9, 20059. / 6000 },
        { 3.0, 10. / 3 }
    };

    double[] xValues1 = ArrayTestUtils.getCol(vvv, 0);
    double[] yValues1 = ArrayTestUtils.getCol(vvv, 1);
    assertThat(spline.values(xValues1)).containsExactly(yValues1, Offset.offset(EPS));
  }

  @Test
  public void shortDataLengthTest() {
    double[] X_KNOTS1 = new double[]{ 1. };
    double[] Y_KNOTS1 = new double[]{ 4. };
    double[] X_KNOTS2 = new double[]{ 1., 2. };
    double[] Y_KNOTS2 = new double[]{ 4., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS1, Y_KNOTS1));

    interpolator.interpolate(X_KNOTS2, Y_KNOTS2);
  }
}
