package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.interest.basis.DayCount;

import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
public class CurveDefinition {
  String curveName;
  DayCount dayCount;
  InterpolationAlgorithm interpolator;
  String zeroLabel;
  List<CurveNodeDefinition> curveNodes;
}
