package org.blacksmith.finlib.curves.types;

import java.util.Comparator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Knot {
  private final int x;
  private final double y;
  public static Comparator<Knot> comparatorByX() {
    return Comparator.comparingInt(Knot::getX);
  }
}
