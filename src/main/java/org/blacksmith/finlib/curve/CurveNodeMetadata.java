package org.blacksmith.finlib.curve;

import org.blacksmith.finlib.basic.datetime.Tenor;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurveNodeMetadata {
//  LocalDate date;
  String label;
  Tenor tenor;
}
