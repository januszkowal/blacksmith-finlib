package org.blacksmith.finlib.curve.node;

import org.blacksmith.finlib.basic.datetime.Tenor;

public interface CurveNodeReferenceData {
  String getLabel();
  Tenor getTenor();
  double getValue();
}
