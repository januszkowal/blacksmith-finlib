package org.blacksmith.finlib.curve.types;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurvePoint {
  final int x;
  final double y;
  final boolean knot;
}
