package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.blacksmith.finlib.curve.CurveDefinition;
import org.blacksmith.finlib.curve.YieldCurveCalculator;
import org.blacksmith.finlib.curve.YieldCurveRate;
import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.Knot;
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
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class YieldCurveCalculatorBenchmark {
  private static final LocalDate asOfDate = LocalDate.now();
  private static final YieldCurveCalculator calculator = new YieldCurveCalculator();

  @State(Scope.Benchmark)
  public static class BenchmarkData {
    @Param({ "1", "10" })
    int curveYears;

    @Param({ "AKIMA_SPLINE_APACHE_COMMONS", "AKIMA_SPLINE_BLACKSMITH", "LINEAR_APACHE_COMMONS", "LINEAR_BLACKSMITH" })
    AlgorithmType algorithm;
    public List<Knot> knots;
    public int min;
    public int max;
    private CurveDefinition curveDefinition;

    @Setup(Level.Trial)
    public void setUp() {
      if (curveYears == 1) {
        this.knots = createKnots1Y();
      }
      else if (curveYears == 10) {
        this.knots = createKnots10Y();
      }
      this.min = knots.stream().mapToInt(Knot::getX).min().getAsInt();
      this.max = knots.stream().mapToInt(Knot::getX).max().getAsInt();
      this.curveDefinition = CurveDefinition.of("BONDS", algorithm, 365);
    }
  }

  @Benchmark
  public List<YieldCurveRate> generateCurve(BenchmarkData data) {
    return calculator.values(asOfDate, data.curveDefinition, data.knots);
  }

  private static List<Knot> createKnots1Y() {
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
        Knot.of(365 * 10, 4.8d));//1Y
  }
}
