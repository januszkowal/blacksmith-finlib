package org.blacksmith.finlib.curves;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.blacksmith.finlib.curves.algoritm.SingleArgumentFunction;
import org.blacksmith.finlib.curves.types.CurvePoint;
import org.blacksmith.finlib.curves.types.Knot;

public class YieldCurveFunction {
  private final Set<Integer> knotSet;
  private SingleArgumentFunction interpolator;

  public YieldCurveFunction(List<Knot> knots, SingleArgumentFunction interpolator) {
    this.knotSet = knots.stream().map(Knot::getX).collect(Collectors.toSet());
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
