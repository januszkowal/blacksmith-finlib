package org.blacksmith.finlib.curves;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curves.algoritm.PolynomialFunction;
import org.blacksmith.finlib.curves.types.CurvePoint;

public interface CurveFunction extends PolynomialFunction {

  boolean isKnot(int x);

  default CurvePoint valueForIntX(int x) {
    return CurvePoint.of(x, value(x), isKnot(x));
  }

  default List<CurvePoint> curveValues(int min, int max) {
    return IntStream.rangeClosed(min, max).boxed()
        .map(x -> valueForIntX(x))
        .collect(Collectors.toList());
  }
}
