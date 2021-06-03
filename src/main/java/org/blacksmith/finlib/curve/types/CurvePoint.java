package org.blacksmith.finlib.curve.types;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurvePoint {
  int x;
  double y;
  boolean knot;
}
