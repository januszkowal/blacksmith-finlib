package org.blacksmith.finlib.curves;

import java.util.ArrayList;
import java.util.List;

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
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 0.1d)).isEqualTo(0);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 1.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 2d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 2.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 3d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 3.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 4d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 4.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 5d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 5.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 6d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 6.1d)).isEqualTo(1);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 7d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearch0(knotsX, 7d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 7.1d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 8d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 8.1d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 9d)).isEqualTo(2);
    assertThat(AlgorithmUtils.binarySearchA(knotsX, 9.1d)).isEqualTo(2);
  }
}
