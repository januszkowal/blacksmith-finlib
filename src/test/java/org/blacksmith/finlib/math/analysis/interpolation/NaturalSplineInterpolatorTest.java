package org.blacksmith.finlib.math.analysis.interpolation;

import org.assertj.core.data.Offset;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Slf4j
public class NaturalSplineInterpolatorTest {

  private static final double EPS = 1e-14;
  private static final double INF = 1. / 0.;
  final InterpolatorFactory factory = new InterpolatorFactory();



  @Test
  public void recov2ptsTest() {
    final double[] xValues = new double[]{ 1., 2. };
    final double[] yValues = new double[]{ 6., 1. };

    final int nIntervals = 1;
    final double[][] coeffsMatrix = new double[][]{ { 0., 0., 6.0, -5.0 } };

    var interpolator = new NaturalSplineInterpolator().interpolate(xValues, yValues);

    assertThat(interpolator.getIntervals()).isEqualTo(nIntervals);
    assertThat(interpolator.getXValues()).containsExactly(xValues);
    PolynomialTestUtils.assertCoeffArray(coeffsMatrix, interpolator);
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

    var interpolator = new NaturalSplineInterpolator().interpolate(xValues, yValues);

    assertThat(interpolator.getIntervals()).isEqualTo(nIntervals);
    assertThat(interpolator.getXValues()).containsExactly(xValues);
    PolynomialTestUtils.assertCoeffArray(coeffsMatrix, interpolator);
    assertThat(interpolator.values(xValues)).containsExactly(yValues, Offset.offset(EPS));

    double[][] vvv = new double[][]{
        { 1., 6. },
        { 1.2, 5.601333333333334 },
        { 1.5, 5.020833333333334 },
        { 1.7, 4.657166666666667 },
        { 2.0, 4.166666666666667 },
        { 2.3, 3.7661666666666673 },
        { 2.5, 3.5625 },
        { 2.7, 3.4188333333333336 },
        { 2.9, 3.343166666666667 },
        { 3.0, 3.3333333333333335 }
    };

    double[] xValues1 = PolynomialTestUtils.getCol(vvv, 0);
    double[] yValues1 = PolynomialTestUtils.getCol(vvv, 1);
    assertThat(interpolator.values(xValues1)).containsExactly(yValues1, Offset.offset(EPS));
  }


  @Test
  public void shortDataLengthTest() {
    double[] xValues = new double[]{ 1. };
    double[] yValues = new double[]{ 4. };

    NaturalSplineInterpolator interp = new NaturalSplineInterpolator();

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interp.interpolate(xValues, yValues));
  }
}
