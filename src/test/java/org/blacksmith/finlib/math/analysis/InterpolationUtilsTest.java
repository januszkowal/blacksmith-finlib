package org.blacksmith.finlib.math.analysis;

import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationUtils;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterpolationUtilsTest {
  @Test
  public void indexSearch1() {
    List<Knot> knots = List.of(Knot.of(0, 0),
        Knot.of(1, 2),
        Knot.of(5, 2.2),
        Knot.of(10, 2.5),
        Knot.of(15, 2.8));
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 0.1d)).isEqualTo(0);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1.1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 2d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 3d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 4d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5.1d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 6d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 8d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 9d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10d)).isEqualTo(3);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10.1d)).isEqualTo(3);
  }

  @Test
  public void indexSearch2() {
    List<Knot> knots = List.of(Knot.of(0, 0), Knot.of(1, 2), Knot.of(5, 2.2), Knot.of(10, 2.5));
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    assertThat(InterpolationUtils.getKnotIndex1(knotsX, 0.1d)).isEqualTo(0);
    assertThat(InterpolationUtils.getKnotIndex1(knotsX, 1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex1(knotsX, 1.1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex1(knotsX, 2d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 3d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 4d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5.1d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 6d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 8d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 9d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10d)).isEqualTo(3);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10.1d)).isEqualTo(3);
  }

  @Test
  public void indexSearch0_1() {
    List<Knot> knots = List.of(Knot.of(0, 0),
        Knot.of(1, 2),
        Knot.of(5, 2.2),
        Knot.of(10, 2.5),
        Knot.of(15, 2.8));
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 0.1d)).isEqualTo(0);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1.1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 2d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 3d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 4d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5.1d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 6d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 8d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 9d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10d)).isEqualTo(3);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10.1d)).isEqualTo(3);
  }

  @Test
  public void indexSearch0_2() {
    List<Knot> knots = List.of(Knot.of(0, 0), Knot.of(1, 2), Knot.of(5, 2.2), Knot.of(10, 2.5));
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 0.1d)).isEqualTo(0);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 1.1d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 2d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 3d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 4d)).isEqualTo(1);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 5.1d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 6d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 8d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 9d)).isEqualTo(2);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10d)).isEqualTo(3);
    assertThat(InterpolationUtils.getKnotIndex0(knotsX, 10.1d)).isEqualTo(3);
  }
}
