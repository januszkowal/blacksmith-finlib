package org.blacksmith.finlib.curves;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curves.algoritm.SingleArgumentFunction;
import org.blacksmith.finlib.curves.types.CurvePoint;
import org.blacksmith.finlib.curves.types.Knot;

public class YieldCurveFunction {
  private final Set<Integer> knotSet;
  private SingleArgumentFunction interpolator;

  public YieldCurveFunction(double[] knots, SingleArgumentFunction interpolator) {
    this.knotSet = Arrays.stream(knots)
        .mapToInt(x -> (int)Math.ceil(x))
        .boxed()
        .collect(Collectors.toSet());
    this.interpolator = interpolator;
  }

  public YieldCurveFunction(List<Integer> knots, SingleArgumentFunction interpolator) {
    this.knotSet = knots.stream().collect(Collectors.toSet());
    this.interpolator = interpolator;
  }

  public CurvePoint value(int x) {
    double y = interpolator.value(x);
    return CurvePoint.of(x, y, isKnot(x));
  }

  private boolean isKnot(int x) {
    return knotSet.contains(x);
  }
}
