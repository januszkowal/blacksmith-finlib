package org.blacksmith.finlib.curve;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curve.algorithm.PolynomialFunction;
import org.blacksmith.finlib.curve.types.CurvePoint;

public interface CurveFunction extends PolynomialFunction {

  boolean isKnot(int x);

  default CurvePoint curvePoint(int x) {
    return CurvePoint.of(x, value(x), isKnot(x));
  }

  default List<CurvePoint> values(int min, int max) {
    return IntStream.rangeClosed(min, max).boxed()
        .map(x -> curvePoint(x))
        .collect(Collectors.toList());
  }
}
