package org.blacksmith.finlib.curves;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.finlib.curves.types.Knot;
import org.junit.jupiter.api.Test;

public class CurveGenerationTest {

  @Test
  public void createCurveWithReflectionPoints() {
    exportCurve(create365DayKnotsWithInflectionPoints(), Path.of("doc/data2.csv"));
  }

  @Test
  public void createCurveWithoutReflectionPoints() {
    exportCurve(create365DayKnots(), Path.of("doc/data1.csv"));
  }

  private List<Knot> create365DayKnots() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));//1D
    knots.add(Knot.of(1, 2.50d));//1D
    knots.add(Knot.of(7, 3.07d));//1D
    knots.add(Knot.of(14, 3.36d));//1W
    knots.add(Knot.of(30, 3.71d));//2W
    knots.add(Knot.of(90, 4.27d));//1M
    knots.add(Knot.of(182, 4.38d));//6M
    knots.add(Knot.of(273, 4.47d));//6M
    knots.add(Knot.of(365, 4.52d));//1Y
    return knots;
  }

  private List<Knot> create365DayKnotsWithInflectionPoints() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));//1D
    knots.add(Knot.of(1, 3.2d));//1D
    knots.add(Knot.of(7, 3.07d));//1D
    knots.add(Knot.of(14, 3.36d));//1W
    knots.add(Knot.of(30, 3.71d));//2W
    knots.add(Knot.of(90, 4.27d));//1M
    knots.add(Knot.of(182, 4.38d));//6M
    knots.add(Knot.of(273, 4.47d));//6M
    knots.add(Knot.of(365, 4.52d));//1Y
    return knots;
  }

  private void exportCurve(List<Knot> knots, Path path) {
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    CurveInterpolatorFactory factory = new CurveInterpolatorFactory();
    var akimaInterpolatorBlackSmith = factory.getInterpolator("AkimaSplineBlackSmith", knots);
    var akimaInterpolatorApacheCommons = factory.getInterpolator("AkimaSplineApacheCommons", knots);
    var linearInterpolatorBlackSmith = factory.getInterpolator("LinearBlackSmith", knots);
    var linearInterpolatorApacheCommons = factory.getInterpolator("LinearApacheCommons", knots);
    var valuesAkimaBlackSmith = IntStream.range(0, maxValue).boxed()
        .map(x -> akimaInterpolatorBlackSmith.value(x))
        .collect(Collectors.toList());
    var valuesAkimaApacheCommons = IntStream.range(0, maxValue).boxed()
        .map(x -> akimaInterpolatorApacheCommons.value(x))
        .collect(Collectors.toList());
    var valuesLinearBlackSmith = IntStream.range(0, maxValue).boxed()
        .map(x -> linearInterpolatorBlackSmith.value(x))
        .collect(Collectors.toList());
    var valuesLinearApacheCommons = IntStream.range(0, maxValue).boxed()
        .map(x -> linearInterpolatorApacheCommons.value(x))
        .collect(Collectors.toList());
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,funAkimaBlacksmith,funAkimaApacheCommons,funLinearBlacksmith,funLinearApacheCommons,knot");
      IntStream.range(0, maxValue).boxed().map(i -> convertToCSV(String.valueOf(i),
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
        .collect(Collectors.joining(","));
  }

}
