package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.interest.basis.DayCount;
import org.blacksmith.finlib.math.analysis.UnivariateFunction;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;

public class CurveImpl implements Curve {
  private final LocalDate valuationDate;
  private final Set<Integer> knotSet;
  private final UnivariateFunction function;
  private final UnivariateFunction leftExtrapolator;
  private final UnivariateFunction rightExtrapolator;
  private final Knot minKnot;
  private final Knot maxKnot;
  private final String name;
  private final DayCount dayCount;

  public CurveImpl(LocalDate valuationDate, String name, DayCount dayCount, InterpolatedFunction function, Knot minKnot, Knot maxKnot) {
    this.valuationDate = valuationDate;
    this.name = name;
    this.dayCount = dayCount;
    this.function = function;
    this.minKnot = minKnot;
    this.maxKnot = maxKnot;
    this.knotSet = Arrays.stream(function.getKnots())
        .mapToObj(x -> (int) Math.ceil(x))
        .collect(Collectors.toSet());
    this.leftExtrapolator = x -> minKnot.getY();
    this.rightExtrapolator = x -> maxKnot.getY();
  }

  @Override
  public double value(double x) {
    if (x < this.minKnot.getX()) {
      return leftExtrapolator.value(x);
    }
    else if (x > this.maxKnot.getX()) {
      return rightExtrapolator.value(x);
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
  public double value(LocalDate date) {
    double x = dayCount.relativeYearFraction(valuationDate, date);
    return value(x);
  }

  @Override
  public DayCount getDayCount() {
    return this.dayCount;
  }

  @Override
  public boolean isKnot(int x) {
    return this.knotSet.contains(x);
  }
}
