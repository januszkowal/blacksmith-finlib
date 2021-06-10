package org.blacksmith.finlib.curve;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.interest.basis.DayCount;

public class CurveImpl implements Curve {
  private final Set<Integer> knotSet;
  private final UnivariateFunction function;
  private final UnivariateFunction leftExtrapolator;
  private final UnivariateFunction rightExtrapolator;
  private final double minX;
  private final double maxX;
  private final String name;
  private final DayCount dayCount;

  public CurveImpl(String name, DayCount dayCount, InterpolatedFunction function) {
    this.name = name;
    this.dayCount = dayCount;
    this.function = function;
    var knotStats = DoubleStream.of(function.getKnots()).summaryStatistics();
    this.minX = knotStats.getMin();
    this.maxX = knotStats.getMax();
    this.knotSet = Arrays.stream(function.getKnots())
        .mapToObj(x -> (int) Math.ceil(x))
        .collect(Collectors.toSet());
    this.leftExtrapolator = (double x) -> function.value(this.minX);
    this.rightExtrapolator = (double x) -> function.value(this.maxX);
  }

//  @Override
//  public double[] getKnots() {
//    return function.getKnots();
//  }

  @Override
  public double value(double x) {
    if (x < this.minX) {
      return leftExtrapolator.value(this.minX);
    }
    else if (x > this.maxX) {
      return rightExtrapolator.value(this.maxX);
    }
    else {
      return function.value(x);
    }
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public DayCount getDayCount() {
    return this.dayCount;
  }

  @Override
  public boolean isKnot(int x) {
    return this.knotSet.contains(x);
  }

//  @Override
//  public List<CurvePoint> values(int min, int max) {
//    return IntStream.rangeClosed(min, max)
//        .mapToObj(x -> CurvePoint.of(x, value(x), isKnot(x)))
//        .collect(Collectors.toList());
//  }
}
