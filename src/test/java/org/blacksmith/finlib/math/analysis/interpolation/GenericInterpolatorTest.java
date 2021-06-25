package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;
import org.blacksmith.test.VariableSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.data.Offset.offset;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericInterpolatorTest {

  private static final double EPS_KNOT = 1e-14;
  private static final double EPS = 1e-7;
  private static final double INF = 1. / 0.;

  /* Used instead of @MethodSource*/
  public static List<Arguments> interpolators = List.of(
      Arguments.of(new LinearInterpolator()),
      Arguments.of(new NaturalSplineInterpolator()),
      Arguments.of(new AkimaSplineInterpolator()),
      Arguments.of(new QuadraticSplineInterpolator()),
      Arguments.of(new DoubleQuadraticInterpolator())
  );

  @ParameterizedTest
  @VariableSource("interpolators")
  public void coincidenceXvaluesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3., 3. };
    double[] Y_KNOTS = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void nullXvaluesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = null;
    double[] Y_KNOTS = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void nullYvaluesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3., 4. };
    double[] Y_KNOTS = null;

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void wrongDatalengthTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3. };
    double[] Y_KNOTS = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void naNxValuesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., Double.NaN, 4. };
    double[] Y_KNOTS = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void naNyValuesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3., 4. };
    double[] Y_KNOTS = new double[]{ 1., 2., Double.NaN, 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void infxValuesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3., INF };
    double[] Y_KNOTS = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void infyValuesTest(PolynomialInterpolator interpolator) {
    double[] X_KNOTS = new double[]{ 1., 2., 3., 4. };
    double[] Y_KNOTS = new double[]{ 1., 2., 3., INF };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(X_KNOTS, Y_KNOTS));
  }

  @ParameterizedTest
  @EnumSource(InterpolationAlgorithm.class)
  public void shouldIncreaseFail(InterpolationAlgorithm algorithm) {
    List<Knot> knots = List.of(Knot.of(0, 0),
        Knot.of(1, 2),
        Knot.of(5, 2.2),
        Knot.of(5, 2.3),
        Knot.of(10, 2.5),
        Knot.of(15, 2.8));
    var factory = new InterpolatorFactory();
    assertThrows(IllegalArgumentException.class, () -> factory.createFunction(algorithm, knots));
  }

  @ParameterizedTest
  @EnumSource(InterpolationAlgorithm.class)
  public void shouldSuccess(InterpolationAlgorithm algorithm) {
    List<Knot> knots = List.of(Knot.of(0, 0),
        Knot.of(1, 2),
        Knot.of(5, 2.2),
        Knot.of(6, 2.3),
        Knot.of(10, 2.5),
        Knot.of(15, 2.8));
    var factory = new InterpolatorFactory();
    Assertions.assertThat(factory.createFunction(algorithm, knots)).isNotNull();
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void nodesValueTest(PolynomialInterpolator interpolator) {
    final double[] X_KNOTS = {0.0, 0.4, 1.0, 1.8, 2.8, 5.0};
    final double[] Y_KNOTS = {3.0, 4.0, 3.1, 2.0, 7.0, 2.0};
    final double[] X_VALUES = {-0.0, 0.0, 0.4, 1.0, 1.8};
    final double[] Y_VALUES = {3.0, 3.0, 4.0, 3.1, 2.0};
    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);
    assertThat(spline.values(X_VALUES)).containsExactly(Y_VALUES, offset(EPS_KNOT));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void outOfRangeArgument(PolynomialInterpolator interpolator) {
    final double[] X_KNOTS = {0.0, 0.4, 1.0, 1.8};
    final double[] Y_KNOTS = {3.0, 4.0, 3.1, 2.0};
    final double illegalXValue = -0.1d;
    var spline = interpolator.interpolate(X_KNOTS, Y_KNOTS);
    spline.value(-1);
    spline.value(2);
  }
}
