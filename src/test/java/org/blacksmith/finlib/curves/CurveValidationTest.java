package org.blacksmith.finlib.curves;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.blacksmith.finlib.curves.types.CurvePoint;
import org.blacksmith.finlib.curves.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class CurveValidationTest {

  CurveFunctionFactory factory = new CurveFunctionFactory();

  @Test
  public void shouldConsecutivePointsYIncrease() {
    var knots = create365DayKnots();
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var akimaInterpolatorBlackSmith = factory.getFunction(CurveType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, maxValue);
    assertThat(maxValue).isEqualTo(365);
    assertThat(points.size()).isEqualTo(maxValue+1);
    int priorX =-1;
    double priorY = 0d;
    for (CurvePoint point: points) {
      assertThat(point.getY()).isGreaterThanOrEqualTo(priorY);
      assertThat(point.getX()).isEqualTo(priorX + 1);
      priorY = point.getY();
      priorX = point.getX();
    }
  }

  @Test
  public void shouldGenerateMinSizeCurve() {
    List<Knot> knots = new ArrayList();
    knots.add(Knot.of(0, 2.43d));
    knots.add(Knot.of(1, 2.50d));
    knots.add(Knot.of(7, 3.07d));
    var akimaInterpolatorBlackSmith = factory.getFunction(CurveType.AKIMA_SPLINE_BLACKSMITH, knots);
    var points = akimaInterpolatorBlackSmith.curveValues(0, 7);
    assertThat(points.size()).isEqualTo(8);
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

  private void exportCurve(List<Knot> knots, Path path) {
    int maxValue = knots.stream().mapToInt(Knot::getX).max().getAsInt();
    var akimaInterpolatorBlackSmith = factory.getFunction(CurveType.AKIMA_SPLINE_BLACKSMITH, knots);
    var akimaInterpolatorApacheCommons = factory.getFunction(CurveType.AKIMA_SPLINE_APACHE_COMMONS, knots);
    var linearInterpolatorBlackSmith = factory.getFunction(CurveType.LINEAR_BLACKSMITH, knots);
    var linearInterpolatorApacheCommons = factory.getFunction(CurveType.LINEAR_APACHE_COMMONS, knots);
    var valuesAkimaBlackSmith = akimaInterpolatorBlackSmith.curveValues(0, maxValue);
    var valuesAkimaApacheCommons = akimaInterpolatorApacheCommons.curveValues(0, maxValue);
    var valuesLinearBlackSmith = linearInterpolatorBlackSmith.curveValues(0, maxValue);
    var valuesLinearApacheCommons = linearInterpolatorApacheCommons.curveValues(0, maxValue);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,funAkimaBlacksmith,funAkimaApacheCommons,funLinearBlacksmith,funLinearApacheCommons,knot");
      IntStream.rangeClosed(0, maxValue).boxed().map(i -> convertToCSV(String.valueOf(i),
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
