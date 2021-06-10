package org.blacksmith.finlib.math.analysis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationUtils;
import org.blacksmith.finlib.curve.types.Knot;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(2)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class AlgorithmUtilsBenchmark {
  private static List<Knot> create365DayKnots() {
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

  @Benchmark
  public int[] getKnotIndexArraysMethod(Data data) {
    int[] results = new int[data.max - data.min + 1];
    for (int i = data.min; i <= data.max; i++) {
      results[i] = InterpolationUtils.getKnotIndex1(data.knotArray, i);
    }
    return results;
  }

  @Benchmark
  public int[] getKnotIndexBlacksmithMethod(Data data) {
    int[] results = new int[data.max - data.min + 1];
    for (int i = data.min; i <= data.max; i++) {
      results[i] = InterpolationUtils.getKnotIndex0(data.knotArray, i);
    }
    return results;
  }

  @State(Scope.Benchmark)
  public static class Data {
    final List<Knot> knotList = create365DayKnots();
    final double[] knotArray = knotList.stream().mapToDouble(Knot::getX).toArray();
    final int min = (int)DoubleStream.of(knotArray).min().orElse(0d);
    final int max = (int)DoubleStream.of(knotArray).max().orElse(0d);
  }
}
