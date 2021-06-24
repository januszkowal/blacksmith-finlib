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
    final double[] xValues = new double[]{ 1., 2., 3. };
    final double[] yValues = new double[]{ 6., 25. / 6., 10. / 3. };

    final int nIntervals = 2;
    final double[][] coeffsMatrix = new double[][]{ { 6., -7. / 3, 0.5, 0 }, { 25. / 6, -4. / 3, 0.5, 0 } };
    var spline = interpolator.interpolate(xValues, yValues);
    System.out.println(ArrayTestUtils.arrayToFractionString(PolynomialTestUtils.getCoeffMatrix(spline, 4), 1e-9));
    assertThat(spline.getIntervals()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(xValues);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
  }

  @Test
  public void recov4ptsTest() {
    final double[] xValues = new double[]{ 1., 2., 3., 4 };
    final double[] yValues = new double[]{ 6., 25. / 6, 10. / 3., 4. };
    //
    final int nIntervals = 3;
    final double[][] coeffsMatrix = new double[][]{ { 6., -7. / 3, 3. / 5, -1. / 10 },
        { 25. / 6, -43. / 30, 3. / 5, 0 },
        { 10. / 3, -7. / 30, 21. / 20, -3. / 20 } };

    var spline = interpolator.interpolate(xValues, yValues);
    //    System.out.println(array2DToFractionString(PolynomialTestUtils.getCoeffMatrix(spline, 4)));

    assertThat(spline.getIntervals()).isEqualTo(nIntervals);
    assertThat(spline.getXValues()).containsExactly(xValues);
    ArrayTestUtils.assertArray(PolynomialTestUtils.getCoeffMatrix(spline, 4), coeffsMatrix, EPS_KNOT);
    assertThat(spline.values(xValues)).containsExactly(yValues, Offset.offset(EPS_KNOT));

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
    double[] xValues1 = new double[]{ 1. };
    double[] yValues1 = new double[]{ 4. };
    double[] xValues2 = new double[]{ 1., 2. };
    double[] yValues2 = new double[]{ 4., 4. };
    double[] xValues3 = new double[]{ 1., 2., 3. };
    double[] yValues3 = new double[]{ 4., 4., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues1, yValues1));

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues2, yValues2));

    interpolator.interpolate(xValues3, yValues3);
  }
}
