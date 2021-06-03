package org.blacksmith.finlib.curve;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.types.CurvePoint;
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
@Warmup(iterations = 3, time = 10)
@Measurement(iterations = 5, time = 10)
public class CurveCalculatorBenchmark {

  @State(Scope.Benchmark)
  public static class BenchmarkData {
    @Param({ "1", "10" })
    int years;

    @Param({ "AKIMA_SPLINE_APACHE_COMMONS", "AKIMA_SPLINE_BLACKSMITH", "LINEAR_APACHE_COMMONS", "LINEAR_BLACKSMITH" })
    AlgorithmType algorithm;
    public List<Knot> knots;
    public int xMin;
    public int xMax;
    public CurveFunction curveFunction;

    @Setup(Level.Trial)
    public void setUp() {
      if (years == 1) {
        this.knots = createKnots1Y();
      }
      else if (years == 10) {
        this.knots = createKnots10Y();
      }
      this.xMin = this.knots.stream().mapToInt(Knot::getX).min().getAsInt();
      this.xMax = this.knots.stream().mapToInt(Knot::getX).max().getAsInt();
      CurveDefinition definition = CurveDefinition.of("BONDS", algorithm, 365);
      this.curveFunction = new CurveFunctionFactory().getCurveFunction(definition.getAlgorithm(), knots);
    }
  }

  @Benchmark
  public List<CurvePoint> generateCurve(BenchmarkData data) {
    return data.curveFunction.values(data.xMin, data.xMax);
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
