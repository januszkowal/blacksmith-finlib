package org.blacksmith.finlib.rates.curve;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.finlib.curves.CurveInterpolatorFactory;
import org.blacksmith.finlib.curves.algoritm.AlgorithmUtils;
import org.blacksmith.finlib.curves.types.Knot;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SomeTests {
  @Test
  public void curvePointTest() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 0));//1D
    knots.add(Knot.of(1, 2));//1D
    knots.add(Knot.of(7, 2.2));//1D
    knots.add(Knot.of(14, 2.5));//1W
    knots.add(Knot.of(30, 2.8));//2W
    knots.add(Knot.of(90, 3.2));//1M
    knots.add(Knot.of(180, 3.6));//6M
    knots.add(Knot.of(360, 4));//1Y
    knots.add(Knot.of(720, 3.99));//1Y
    knots.add(Knot.of(3600, 4.5));//1Y
    knots.add(Knot.of(10000, 4.2));//1Y
    //    var knotsArray = knots.toArray(Point2D[]::new);
    //    List<Point2D> knotsPoints = Stream.of(knotsArray).map(knot -> Point2D.of(knot.getX(), knot.getY())).sorted(Point2D.comparatorByX()).collect(Collectors.toList());
    CurveInterpolatorFactory factory = new CurveInterpolatorFactory();
    var interpolator = factory.getInterpolator("AkimaSplineBS2", knots);
    //    var knotsX = knotsPoints.stream().mapToDouble(Point2D::getX).toArray();
    //    var knotsY = knotsPoints.stream().mapToDouble(Point2D::getY).toArray();
    //    AkimaSplineInterpolatorBS interpolatorBS = new AkimaSplineInterpolatorBS(knotsX, knotsY);
    var values = IntStream.range(0, 100).boxed()
        .map(x -> interpolator.value(x))
        .map(Objects::toString)
        .collect(Collectors.joining("\n", "", ""));
    //        .collect(Collectors.toList());
    System.out.print(values);
  }

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
    //    knots.add(Knot.of(720,3.99));//1Y
    //    knots.add(Knot.of(3600,4.5));//1Y
    //    knots.add(Knot.of(10000,4.2));//1Y
    var knotsX = knots.stream().mapToDouble(Knot::getX).toArray();
    //    assertThat(binarySearch2(knotsX, 0d)).isEqualTo(0);
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

  @Test
  public void createCurve() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 0));//1D
    knots.add(Knot.of(1, 2));//1D
    knots.add(Knot.of(7, 2.2));//1D
    knots.add(Knot.of(14, 2.5));//1W
    knots.add(Knot.of(30, 2.8));//2W
    knots.add(Knot.of(90, 3.2));//1M
    knots.add(Knot.of(180, 3.6));//6M
    knots.add(Knot.of(360, 4));//1Y
    knots.add(Knot.of(720, 3.99));//1Y
    knots.add(Knot.of(3600, 4.5));//1Y
    knots.add(Knot.of(10000, 4.2));//1Y
    int minValue = 0;
    int maxValue = 10000;
    CurveInterpolatorFactory factory = new CurveInterpolatorFactory();
    var akimaInterpolatorBlackSmith = factory.getInterpolator("AkimaSplineBlackSmith", knots);
    var akimaInterpolatorApacheCommons = factory.getInterpolator("AkimaSplineApacheCommons", knots);
    var linearInterpolatorBlackSmith = factory.getInterpolator("LinearBlackSmith", knots);
    var linearInterpolatorApacheCommons = factory.getInterpolator("LinearApacheCommons", knots);
    var valuesAkimaBlackSmith = IntStream.range(minValue, maxValue).boxed()
        .map(x -> akimaInterpolatorBlackSmith.value(x))
        .collect(Collectors.toList());
    var valuesAkimaApacheCommons = IntStream.range(minValue, maxValue).boxed()
        .map(x -> akimaInterpolatorApacheCommons.value(x))
        .collect(Collectors.toList());
    var valuesLinearBlackSmith = IntStream.range(minValue, maxValue).boxed()
        .map(x -> linearInterpolatorBlackSmith.value(x))
        .collect(Collectors.toList());
    var valuesLinearApacheCommons = IntStream.range(minValue, maxValue).boxed()
        .map(x -> linearInterpolatorApacheCommons.value(x))
        .collect(Collectors.toList());
    Path path = Paths.get("data1.csv");
    //    File csvOutputFile = new File(CSV_FILE_NAME);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,funAkimaBlackSmith,funAkimaApacheCommons,funLinearBlackSmith,funLinearApacheCommons,knot");
      IntStream.range(minValue, maxValue).boxed().map(i -> convertToCSV(String.valueOf(i),
          String.valueOf(valuesAkimaBlackSmith.get(i).getY()),
          String.valueOf(valuesAkimaApacheCommons.get(i).getY()),
          String.valueOf(valuesLinearBlackSmith.get(i).getY()),
          String.valueOf(valuesLinearApacheCommons.get(i).getY()),
          valuesAkimaBlackSmith.get(i).isKnot() ? String.valueOf(valuesAkimaBlackSmith.get(i).getY()) : ""))
          .forEach(pw::println);
    } catch (Exception e) {

    }
  }

  public String convertToCSV(String... data) {
    return Stream.of(data)
        //        .map(this::escapeSpecialCharacters)
        .collect(Collectors.joining(","));
  }

}
