package org.blacksmith.finlib.curve.node;

import org.blacksmith.finlib.basic.datetime.Tenor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurveNodeMetadata {
  String label;
  Tenor tenor;
}
