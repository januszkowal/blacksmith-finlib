package org.blacksmith.finlib.math.struct;

import java.util.function.BiFunction;

import org.blacksmith.finlib.math.struct.FieldOperations;

public class DoubleOperations implements FieldOperations<Double> {
  @Override
  public BiFunction<Double, Double, Double> addition() {
    return (a, b) -> a + b;
  }
  @Override
  public BiFunction<Double, Double, Double> subtraction() {
    return (a, b) -> a - b;
  }

  @Override
  public BiFunction<Double, Double, Double> multiplication() {
    return (a, b) -> a * b;
  }

  @Override
  public BiFunction<Double, Double, Double> division() {
    return (a, b) -> a / b;
  }

  @Override
  public Double factory(double value) {
    return value;
  }

  @Override
  public boolean isZero(Double value) {
    return value.doubleValue() == 0.0;
  }
}
