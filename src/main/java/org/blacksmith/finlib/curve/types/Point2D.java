package org.blacksmith.finlib.curve.types;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class Point2D {
  private final double x;
  private final double y;
}
