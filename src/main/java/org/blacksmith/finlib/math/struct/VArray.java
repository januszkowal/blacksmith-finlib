package org.blacksmith.finlib.math.struct;

import java.lang.reflect.Array;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VArray<T> {
  private final FieldOperations<T> operations;
  private final Class<T> clazz;
  private T[] arr;

  public VArray(T[] src, Class<T> clazz, FieldOperations<T> operations) {
    this.clazz = clazz;
    this.arr = copyArray(src);
    this.operations = operations;
  }

  private T[] copyArray(T[] src) {
    T[] newArray = (T[])Array.newInstance(clazz, src.length);
    System.arraycopy(src, 0, newArray, 0, src.length);
    return newArray;
  }

  private T[] newArray() {
    return (T[])Array.newInstance(clazz, arr.length);
  }

  public VArray<T> multiply(T multiplicand) {
    return op(multiplicand, operations.multiplication());
  }

  public VArray<T> multiply(VArray<T> multiplicand) {
    return op(multiplicand, operations.multiplication());
  }

  public VArray<T> subtract(T subtrahend) {
    return op(subtrahend, operations.subtraction());
  }

  public VArray<T> subtract(VArray<T> subtrahend) {
    return op(subtrahend, operations.subtraction());
  }

  public VArray<T> divide(T multiplicand) {
    return op(multiplicand, operations.division());
  }

  public VArray<T> divide(VArray<T> divisor) {
    return op(divisor, operations.division());
  }

  private VArray<T> op(T operand, BiFunction<T, T, T> function) {
    T[] copy = newArray();
    for (int i = 0; i < arr.length; i++) {
      copy[i] = function.apply(arr[i], operand);
    }
    return new VArray<T>(copy, clazz, operations);
  }

  private VArray<T> op(VArray<T> operand, BiFunction<T, T, T> operation) {
    T[] copy = newArray();
    for (int i = 0; i < arr.length; i++) {
      copy[i] = operation.apply(arr[i], operand.get(i));
    }
    return new VArray<T>(copy, clazz, operations);
  }

  public T get(int index) {
    return arr[index];
  }

  public int size() {
    return arr.length;
  }

  public T[] toArray() {
    return this.arr;
  }

  @Override
  public String toString() {
    return Stream.of(arr).map(d -> d.toString()).collect(Collectors.joining(",", "Varray[","]"));
  }
}
