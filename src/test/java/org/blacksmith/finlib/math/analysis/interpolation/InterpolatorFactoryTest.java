package org.blacksmith.finlib.math.analysis.interpolation;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class InterpolatorFactoryTest {
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
}
