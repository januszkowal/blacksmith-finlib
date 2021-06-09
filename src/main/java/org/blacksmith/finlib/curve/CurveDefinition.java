package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.interest.basis.DayCount;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveDefinition {
  String curveName;
  DayCount dayCount;
  AlgorithmType algorithm;
  List<CurveNodeDefinition> curveNodes;
}
