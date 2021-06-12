package org.blacksmith.finlib.curve.node;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;

import lombok.Value;

@Value
public class CurveNode {
  LocalDate date;
  double x;
  double y;
  CurveNodeMetadata metadata;

  private CurveNode(LocalDate date, double x, double y, CurveNodeMetadata metadata) {
    ArgChecker.notNull(date);
    ArgChecker.notNull(metadata);
    this.date= date;
    this.x = x;
    this.y = y;
    this.metadata = metadata;
  }

  public static CurveNode of(LocalDate date, double x, double y, CurveNodeMetadata metadata) {
    return new CurveNode(date, x, y, metadata);
  }
}
