package org.blacksmith.finlib.curves.algoritm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curves.types.CurvePoint;

public interface SingleArgumentFunction {
  double value(double x);

  double[] getKnots();

  default List<CurvePoint> values(int min, int max) {
    var knotsSet = Arrays.stream(getKnots())
        .mapToInt(x -> (int)Math.ceil(x))
        .boxed()
        .collect(Collectors.toSet());

    return IntStream.rangeClosed(min, max).boxed()
        .map(x -> CurvePoint.of(x, value(x), knotsSet.contains(x)))
        .collect(Collectors.toList());
  }
}
