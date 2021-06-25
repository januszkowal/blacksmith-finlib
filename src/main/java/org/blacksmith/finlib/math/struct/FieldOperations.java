package org.blacksmith.finlib.math.struct;

import java.util.function.BiFunction;

public interface FieldOperations<T> {
  BiFunction<T, T, T> addition();

  BiFunction<T, T, T> subtraction();

  BiFunction<T, T, T> multiplication();

  BiFunction<T, T, T> division();

  T factory(double value);

  boolean isZero(T value);

  default T add(T value, T augend) {
    return addition().apply(value, augend);
  }

  default T subtract(T value, T subtrahend) {
    return subtraction().apply(value, subtrahend);
  }

  default T multiply(T value, T multiplicand) {
    return multiplication().apply(value, multiplicand);
  }

  default T divide(T value, T divisor) {
    return division().apply(value, divisor);
  }
}
