package org.blacksmith.finlib.curve;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.blacksmith.finlib.curve.algorithm.AlgorithmUtils;
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
public class AlgorithUtilsBenchmark {
  @State(Scope.Benchmark)
  public static class Data
  {
    List<Knot> knots = create365DayKnots();
    double[] knotx = knots.stream().mapToDouble(Knot::getX).toArray();
    int min = knots.stream().mapToInt(Knot::getX).min().getAsInt();
    int max = knots.stream().mapToInt(Knot::getX).max().getAsInt();
  }

  @Benchmark
  public int[] getKnotIndexArraysMethod(Data data) {
    int[] results = new int[data.max - data.min +1];
    for (int i = data.min; i <= data.max; i++) {
      results[i] = AlgorithmUtils.getKnotIndex(data.knotx, i);
    }
    return results;
  }

  private static List<Knot> create365DayKnots() {
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
}
