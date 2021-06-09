package org.blacksmith.finlib.curve.types;

import lombok.Value;

@Value(staticConstructor = "of")
public class Knot implements Comparable<Knot>{
  double x;
  double y;

  @Override
  public int compareTo(Knot o) {
    return Double.compare(this.x, o.getX());
  }
}
