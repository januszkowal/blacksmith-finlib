package org.blacksmith.finlib.curve.algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curve.types.CurvePoint;

public interface PolynomialFunctionExt extends PolynomialFunction {
  default List<CurvePoint> values(int min, int max) {
    Set<Integer> knots = Arrays.stream(getKnots())
        .mapToObj(x -> (int) Math.ceil(x))
        .collect(Collectors.toSet());
    return IntStream.rangeClosed(min, max)
        .mapToObj(x -> CurvePoint.of(x, value(x), knots.contains(x)))
        .collect(Collectors.toList());
  }
}
