package org.blacksmith.finlib.curve;

import java.util.List;

import org.blacksmith.finlib.curve.algorithm.AlgorithmType;
import org.blacksmith.finlib.curve.algorithm.InterpolatedFunction;
import org.blacksmith.finlib.curve.algorithm.InterpolatorFactory;

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
