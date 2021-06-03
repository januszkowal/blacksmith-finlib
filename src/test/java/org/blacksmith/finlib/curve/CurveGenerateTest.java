package org.blacksmith.finlib.curve;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CurveGenerateTest {

  CurveFunctionFactory factory = new CurveFunctionFactory();

  @Test
  public void createCurveWithReflectionPoints() {
    exportCurve(create365DayKnotsWithInflectionPoints(), Path.of("data_rp.csv"));
  }

  @Test
  public void createCurveWithoutReflectionPoints() {
    exportCurve(create365DayKnots(), Path.of("data.csv"));
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
    var akimaInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, knots);
    var akimaInterpolatorApacheCommons = factory.getCurveFunction(AlgorithmType.AKIMA_SPLINE_APACHE_COMMONS, knots);
    var linearInterpolatorBlackSmith = factory.getCurveFunction(AlgorithmType.LINEAR_BLACKSMITH, knots);
    var linearInterpolatorApacheCommons = factory.getCurveFunction(AlgorithmType.LINEAR_APACHE_COMMONS, knots);
    var valuesAkimaBlackSmith = akimaInterpolatorBlackSmith.values(0, maxValue);
    var valuesAkimaApacheCommons = akimaInterpolatorApacheCommons.values(0, maxValue);
    var valuesLinearBlackSmith = linearInterpolatorBlackSmith.values(0, maxValue);
    var valuesLinearApacheCommons = linearInterpolatorApacheCommons.values(0, maxValue);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,funAkimaBlacksmith,funAkimaApacheCommons,funLinearBlacksmith,funLinearApacheCommons,knot");
      IntStream.rangeClosed(0, maxValue).mapToObj(i -> convertToCSV(String.valueOf(i),
          String.valueOf(valuesAkimaBlackSmith.get(i).getY()),
          String.valueOf(valuesAkimaApacheCommons.get(i).getY()),
          String.valueOf(valuesLinearBlackSmith.get(i).getY()),
          String.valueOf(valuesLinearApacheCommons.get(i).getY()),
          valuesAkimaBlackSmith.get(i).isKnot() ? String.valueOf(valuesAkimaBlackSmith.get(i).getY()) : ""))
          .forEach(pw::println);
      System.out.println("Saved to: " + path.toAbsolutePath());
    } catch (Exception e) {
    }
  }

  public String convertToCSV(String... data) {
    return Stream.of(data)
        .collect(Collectors.joining(","));
  }

}
