package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.interest.basis.DayCount;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
public class CurveDefinition {
  @NonNull
  String curveName;
  @NonNull
  DayCount dayCount;
  @NonNull
  InterpolationAlgorithm interpolator;
  @NonNull
  List<CurveNodeDefinition> curveNodes;
}
