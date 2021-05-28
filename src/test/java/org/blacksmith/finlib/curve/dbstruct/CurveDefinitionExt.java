package org.blacksmith.finlib.curve.dbstruct;

import java.util.List;

import org.blacksmith.finlib.curve.algoritm.AlgorithmType;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveDefinitionExt {
  String curveName;
  AlgorithmType curveType;
  int yearLength;
  List<KnotDefinition> knots;
}
