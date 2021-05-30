package org.blacksmith.finlib.curve;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.blacksmith.finlib.curve.algorithm.PolynomialFunction;
import org.blacksmith.finlib.curve.types.CurvePoint;

public interface CurveFunction extends PolynomialFunction {

  boolean isKnot(int x);

//  default CurvePoint valueForIntX(int x) {
//    return CurvePoint.of(x, value(x), isKnot(x));
//  }

  List<CurvePoint> values(int min, int max);
//  default List<CurvePoint> values(int min, int max) {
//    return IntStream.rangeClosed(min, max).boxed()
//        .map(x -> valueForIntX(x))
//        .collect(Collectors.toList());
//  }
}
