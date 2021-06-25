package org.blacksmith.finlib.math.analysis;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InterpolatorGenerateTest {

  final LocalDate valuationDate = LocalDate.now();
  final InterpolatorFactory factory = new InterpolatorFactory();

  @Test
  public void createCurveWithReflectionPoints() {
    exportCurve(create365DayKnotsWithInflectionPoints(), Path.of("data_rp.csv"));
  }

  @Test
  public void createCurveWithoutReflectionPoints() {
    exportCurve(create365DayKnots(), Path.of("data.csv"));
  }

  public String convertToCSV(String... data) {
    return String.join(",", data);
  }

  private List<Knot> create365DayKnots() {
    return List.of(Knot.of(0, 2.43d),
        Knot.of(1, 2.50d),
        Knot.of(7, 2.9d),
//        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d));
  }

  private List<Knot> create365DayKnotsWithInflectionPoints() {
    return List.of(Knot.of(0, 2.43d),
        Knot.of(1, 3.2d),
        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d));
  }

  private List<CurvePoint> getValues(InterpolatedFunction function, LocalDate vd, int min, int max) {
    return IntStream.rangeClosed(min, max)
        .mapToObj(x -> CurvePoint.of(vd.plusDays(x), x, function.value(x)))
        .collect(Collectors.toList());
  }

  private void exportCurve(List<Knot> knots, Path path) {
    double[] xValues = knots.stream().mapToDouble(Knot::getX).toArray();
    double[] yValues = knots.stream().mapToDouble(Knot::getY).toArray();
    Set<Double> knotsX = DoubleStream.of(xValues).boxed().collect(Collectors.toSet());
    int maxValue = knots.stream().mapToInt(x -> (int)x.getX()).max().getAsInt();
    var akimaInterpolatorApacheCommons = factory.createFunction(InterpolationAlgorithm.AKIMA_SPLINE_APACHE_COMMONS, xValues, yValues);
    var akimaInterpolator = factory.createFunction(InterpolationAlgorithm.AKIMA_SPLINE, xValues, yValues);
    var linearInterpolator = factory.createFunction(InterpolationAlgorithm.LINEAR, xValues, yValues);
    var quadraticInterpolator = factory.createFunction(InterpolationAlgorithm.QUADRATIC_SPLINE, xValues, yValues);
    var doubleQuadraticInterpolator = factory.createFunction(InterpolationAlgorithm.DOUBLE_QUADRATIC, xValues, yValues);
    var naturalInterpolator = factory.createFunction(InterpolationAlgorithm.NATURAL_SPLINE, xValues, yValues);
    var valuesAkimaApacheCommons = getValues(akimaInterpolatorApacheCommons, valuationDate, 0, 365);
    var valuesAkima = getValues(akimaInterpolator, valuationDate, 0, 365);
    var valuesLinear = getValues(linearInterpolator, valuationDate, 0, 365);
    var valuesQuadratic = getValues(quadraticInterpolator, valuationDate, 0, 365);
    var valuesDoubleQuadratic = getValues(doubleQuadraticInterpolator, valuationDate, 0, 365);
    var valuesNatural = getValues(naturalInterpolator, valuationDate, 0, 365);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,yAkima,yAkimaApacheCommons,yLinear,yQuadratic,yDoubleQuadratic,yNatural,knot");
      IntStream.rangeClosed(0, maxValue).mapToObj(i -> convertToCSV(String.valueOf(i),
          String.valueOf(valuesAkima.get(i).getY()),
          String.valueOf(valuesAkimaApacheCommons.get(i).getY()),
          String.valueOf(valuesLinear.get(i).getY()),
          String.valueOf(valuesQuadratic.get(i).getY()),
          String.valueOf(valuesDoubleQuadratic.get(i).getY()),
          String.valueOf(valuesNatural.get(i).getY()),
          knotsX.contains(valuesAkima.get(i).getX()) ? String.valueOf(valuesAkima.get(i).getY()) : ""))
          .forEach(pw::println);
      System.out.println("Saved to: " + path.toAbsolutePath());
    } catch (Exception e) {
      log.error("Error saving curve to file: " + path.toAbsolutePath(), e);
    }
  }

}
