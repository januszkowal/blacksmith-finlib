package org.blacksmith.finlib.curve;

import org.blacksmith.finlib.basic.datetime.Tenor;

import lombok.Value;

@Value(staticConstructor = "of")
public class SimpleCurveNodeReferenceData implements CurveNodeReferenceData {
  String label;
  Tenor tenor;
  double value;
}
