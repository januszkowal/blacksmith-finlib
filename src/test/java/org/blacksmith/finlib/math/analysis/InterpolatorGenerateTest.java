package org.blacksmith.finlib.math.analysis;

import java.io.PrintWriter;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import org.blacksmith.finlib.math.analysis.interpolation.AlgorithmType;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatorFactory;
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
        Knot.of(7, 3.07d),
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
    var akimaInterpolatorBlackSmith = factory.createFunction(AlgorithmType.AKIMA_SPLINE_BLACKSMITH, xValues, yValues);
    var akimaInterpolatorApacheCommons = factory.createFunction(AlgorithmType.AKIMA_SPLINE_APACHE_COMMONS, xValues, yValues);
    var linearInterpolatorBlackSmith = factory.createFunction(AlgorithmType.LINEAR_BLACKSMITH, xValues, yValues);
    var linearInterpolatorApacheCommons = factory.createFunction(AlgorithmType.LINEAR_APACHE_COMMONS, xValues, yValues);
    var valuesAkimaBlackSmith = getValues(akimaInterpolatorBlackSmith, valuationDate, 0, 365);
    var valuesAkimaApacheCommons = getValues(akimaInterpolatorApacheCommons, valuationDate, 0, 365);
    var valuesLinearBlackSmith = getValues(linearInterpolatorBlackSmith, valuationDate, 0, 365);
    var valuesLinearApacheCommons = getValues(linearInterpolatorApacheCommons, valuationDate, 0, 365);
    try (PrintWriter pw = new PrintWriter(path.toFile())) {
      pw.println("x,funAkimaBlacksmith,funAkimaApacheCommons,funLinearBlacksmith,funLinearApacheCommons,knot");
      IntStream.rangeClosed(0, maxValue).mapToObj(i -> convertToCSV(String.valueOf(i),
          String.valueOf(valuesAkimaBlackSmith.get(i).getY()),
          String.valueOf(valuesAkimaApacheCommons.get(i).getY()),
          String.valueOf(valuesLinearBlackSmith.get(i).getY()),
          String.valueOf(valuesLinearApacheCommons.get(i).getY()),
          knotsX.contains(valuesAkimaBlackSmith.get(i).getX()) ? String.valueOf(valuesAkimaBlackSmith.get(i).getY()) : ""))
          .forEach(pw::println);
      System.out.println("Saved to: " + path.toAbsolutePath());
    } catch (Exception e) {
      log.error("Error saving curve to file: " + path.toAbsolutePath(), e);
    }
  }

}
