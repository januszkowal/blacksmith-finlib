package org.blacksmith.finlib.math.interpolation;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.curve.types.CurvePoint;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(2)
@Warmup(iterations = 3, time = 10)
@Measurement(iterations = 5, time = 10)
public class InterpolationBenchmark {

  @State(Scope.Benchmark)
  public static class BenchmarkData {
    @Param({ "1", "10" })
    int years;

    @Param({ "AKIMA_SPLINE_APACHE_COMMONS", "AKIMA_SPLINE", "LINEAR" })
    InterpolationAlgorithm interpolator;
    public List<Knot> knots;
    public int xMin;
    public int xMax;
    public InterpolatedFunction function;

    @Setup(Level.Trial)
    public void setUp() {
      if (years == 1) {
        this.knots = createKnots1Y();
      }
      else if (years == 10) {
        this.knots = createKnots10Y();
      }
      DoubleSummaryStatistics knotStats = knots.stream().mapToDouble(knot -> knot.getX()).summaryStatistics();
      this.xMin = (int)knotStats.getMin();
      this.xMax = (int)knotStats.getMax();
      this.function = new InterpolatorFactory().createFunction(interpolator, knots);
    }
  }

  @Benchmark
  public List<CurvePoint> generateCurve(BenchmarkData data) {
    LocalDate valuationDate = LocalDate.now();
    return IntStream.rangeClosed(data.xMin, data.xMax)
        .mapToObj(x -> CurvePoint.of(valuationDate.plusDays(x), (double)x, data.function.value(x)))
        .collect(Collectors.toList());
  }

  private static List<Knot> createKnots1Y() {
    return List.of(Knot.of(0, 2.40d),
        Knot.of(1, 2.50d),
        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d));
  }

  private static List<Knot> createKnots10Y() {
    return List.of(Knot.of(0, 2.43d),
        Knot.of(1, 2.50d),
        Knot.of(7, 3.07d),
        Knot.of(14, 3.36d),
        Knot.of(30, 3.71d),
        Knot.of(90, 4.27d),
        Knot.of(182, 4.38d),
        Knot.of(273, 4.47d),
        Knot.of(365, 4.52d),
        Knot.of(365 * 2, 4.55d),
        Knot.of(365 * 3, 4.59d),
        Knot.of(365 * 5, 4.7d),
        Knot.of(365 * 10, 4.8d));
  }
}
