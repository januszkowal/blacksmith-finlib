package org.blacksmith.finlib.curve.types;

import java.time.LocalDate;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurvePoint {
  LocalDate date;
  double x;
  double y;
}
