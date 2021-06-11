package org.blacksmith.finlib.curve.node;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.CurveNodeMetadata;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveNode {
  LocalDate date;
  double x;
  double y;
  CurveNodeMetadata metadata;

  public CurveNode(LocalDate date, double x, double y, CurveNodeMetadata metadata) {
    ArgChecker.notNull(date);
    ArgChecker.notNull(metadata);
    this.date= date;
    this.x = x;
    this.y = y;
    this.metadata = metadata;
  }
}
