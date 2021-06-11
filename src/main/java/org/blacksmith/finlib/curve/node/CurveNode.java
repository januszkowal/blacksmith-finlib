package org.blacksmith.finlib.curve.node;

import java.time.LocalDate;

import org.blacksmith.finlib.curve.CurveNodeMetadata;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveNode {
  LocalDate date;
  double x;
  double y;
  CurveNodeMetadata metadata;
}
