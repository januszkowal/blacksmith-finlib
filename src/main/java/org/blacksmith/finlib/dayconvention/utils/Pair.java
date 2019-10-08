package org.blacksmith.finlib.dayconvention.utils;

import lombok.Data;

@Data
public class Pair<T> {
  T value1;
  T value2;
  public Pair(T value1, T value2) {
    this.value1 = value1;
    this.value2 = value2;
  }
  public static <T> Pair of(T value1, T value2) {
    return new Pair<T>(value1,value2);
  }
}
