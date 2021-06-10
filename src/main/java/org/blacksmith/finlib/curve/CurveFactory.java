package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.math.analysis.interpolation.AlgorithmType;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatedFunction;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatorFactory;

public class CurveFactory {
  public Curve createCurve(CurveDefinition definition) {
    List<CurveNode> nodes = getCurveNodes(definition);
    var function = createInterpolatorFunction(definition.getAlgorithm(), nodes);
    return new CurveImpl(definition.getCurveName(), definition.getDayCount(), function);
  }

  private InterpolatedFunction createInterpolatorFunction(AlgorithmType algorithmType, List<CurveNode> nodes) {
    var xValues = nodes.stream().mapToDouble(CurveNode::getX).toArray();
    var yValues = nodes.stream().mapToDouble(CurveNode::getY).toArray();
    return (new InterpolatorFactory()).createFunction(algorithmType, xValues, yValues);
  }

  private List<CurveNode> getCurveNodes(CurveDefinition definition) {
    return null;
  }
}
