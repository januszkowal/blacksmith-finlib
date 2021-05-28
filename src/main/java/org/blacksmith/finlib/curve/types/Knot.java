package org.blacksmith.finlib.curve.types;

import lombok.Value;

@Value(staticConstructor = "of")
public class Knot implements Comparable<Knot>{
  private final int x;
  private final double y;

  @Override
  public int compareTo(Knot o) {
    return Integer.compare(this.x, o.getX());
  }
}
