package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.AlgorithmType;
import org.blacksmith.finlib.interest.basis.DayCount;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveDefinition {
  String curveName;
  DayCount dayCount;
  AlgorithmType algorithm;
  List<CurveNodeDefinition> curveNodes;
}
