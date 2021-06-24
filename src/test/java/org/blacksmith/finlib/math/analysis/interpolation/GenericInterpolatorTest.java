package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;
import org.blacksmith.test.VariableSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GenericInterpolatorTest {

  private static final double INF = 1. / 0.;

  /* Used instead of @MethodSource*/
  public static List<Arguments> interpolators = List.of(Arguments.of(new LinearInterpolator()),
      Arguments.of(new NaturalSplineInterpolator()),
      Arguments.of(new AkimaSplineInterpolator()));

  @ParameterizedTest
  @VariableSource("interpolators")
  public void coincidenceXvaluesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3., 3. };
    double[] yValues = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void nullXvaluesTest(PolynomialInterpolator interpolator) {
    double[] xValues = null;
    double[] yValues = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void nullYvaluesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3., 4. };
    double[] yValues = null;

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void wrongDatalengthTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3. };
    double[] yValues = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void naNxValuesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., Double.NaN, 4. };
    double[] yValues = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void naNyValuesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3., 4. };
    double[] yValues = new double[]{ 1., 2., Double.NaN, 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void infxValuesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3., INF };
    double[] yValues = new double[]{ 1., 2., 3., 4. };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
  }

  @ParameterizedTest
  @VariableSource("interpolators")
  public void infyValuesTest(PolynomialInterpolator interpolator) {
    double[] xValues = new double[]{ 1., 2., 3., 4. };
    double[] yValues = new double[]{ 1., 2., 3., INF };

    assertThatIllegalArgumentException()
        .isThrownBy(() -> interpolator.interpolate(xValues, yValues));
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
}
