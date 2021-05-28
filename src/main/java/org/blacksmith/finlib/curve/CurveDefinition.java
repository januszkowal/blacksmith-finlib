package org.blacksmith.finlib.curve;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveDefinition {
  String curveName;
  AlgorithmType algorithm;
  int yearLength;
}
