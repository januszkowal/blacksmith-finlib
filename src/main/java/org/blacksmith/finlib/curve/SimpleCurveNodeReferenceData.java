package org.blacksmith.finlib.curve;

import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;

import lombok.Value;

@Value(staticConstructor = "of")
public class SimpleCurveNodeReferenceData implements CurveNodeReferenceData {
  String label;
  Tenor tenor;
  double value;
}
