package org.blacksmith.finlib.curve.node;

import org.blacksmith.finlib.basic.datetime.Tenor;

public interface CurveNodeDefinition {
  String getLabel();
  Tenor getTenor();
  double spread();
}
