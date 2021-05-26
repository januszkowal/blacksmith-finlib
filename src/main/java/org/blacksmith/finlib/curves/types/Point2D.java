package org.blacksmith.finlib.curves.types;

import java.util.Comparator;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class Point2D {
  private final double x;
  private final double y;

  public static Comparator<Point2D> comparatorByX() {
    return Comparator.comparingDouble(Point2D::getX);
  }
}
