package org.blacksmith.finlib.curves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curves.algoritm.AlgorithmUtils;
import org.blacksmith.finlib.curves.types.Knot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CurveUtilsTest {
  @Test
  public void indexSearch() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 0));//1D
    knots.add(Knot.of(1, 2));//1D
    knots.add(Knot.of(7, 2.2));//1D
    knots.add(Knot.of(14, 2.5));//1W
    knots.add(Knot.of(30, 2.8));//2W
    knots.add(Knot.of(90, 3.2));//1M
    knots.add(Knot.of(180, 3.6));//6M
    knots.add(Knot.of(360, 4));//1Y
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 0.1d)).isEqualTo(0);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 1.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 2d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 2.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 3d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 3.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 4d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 4.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 5d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 5.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 6d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 6.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 7d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 7.1d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 8d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 8.1d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 9d)).isEqualTo(2);
    assertThat(AlgorithmUtils.getKnotIndex0(knotsX, 9.1d)).isEqualTo(2);
  }

  @Test
  public void roundingTest() {
//    assertThat(2d).isEqualTo(Math.floor(2.1d));
//    assertThat(2d).isEqualTo(Math.floor(2d));
    // next
    assertThat(3d).isEqualTo(Math.ceil(2.5d));
    assertThat(3d).isEqualTo(Math.ceil(2.1d));
    assertThat(2d).isEqualTo(Math.ceil(2.0d));
//    assertThat(3d).isEqualTo(Math.round(2.5d));
//    assertThat(2d).isEqualTo(Math.round(2.2d));
//    assertThat(2d).isEqualTo(Math.round(1.9d));
    double[] knots = {0, 1.2, 3.5, 8.2};
    System.out.println(Arrays.stream(knots).mapToInt(x -> (int)x).boxed().collect(Collectors.toList()));
  }
}
