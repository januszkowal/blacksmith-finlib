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
    final double[] xValues = new double[]{ 1., 2. };
    final double[] yValues = new double[]{ 6., 1. };

    final int nIntervals = 1;
    final double[][] coeffsMatrix = new double[][]{ { 0., 0., 6.0, -5.0 } };

    var spline = interpolator.interpolate(xValues, yValues);

    assertThat(spline.getIntervals()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(xValues);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
  }

  @Test
  public void recov4ptsTest() {
    final double[] xValues = new double[]{ 1., 2., 3., 4 };
    final double[] yValues = new double[]{ 6., 25. / 6., 10. / 3., 4. };
    //
    final int nIntervals = 3;
    final double[][] coeffsMatrix =
        new double[][]{ { 6., -2., 0., 1. / 6. },
            { 25. / 6., -3. / 2., 1. / 2., 1. / 6. },
            { 10. / 3., 0., 1., -1. / 3. } };

    var spline = interpolator.interpolate(xValues, yValues);

    assertThat(spline.getIntervals()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(xValues);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
    assertThat(spline.values(xValues)).containsExactly(yValues, Offset.offset(EPS_KNOT));

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
    double[] xValues1 = new double[]{ 1. };
    double[] yValues1 = new double[]{ 4. };
    double[] xValues2 = new double[]{ 1., 2. };
    double[] yValues2 = new double[]{ 4., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues1, yValues1));

    interpolator.interpolate(xValues2, yValues2);
  }
}
