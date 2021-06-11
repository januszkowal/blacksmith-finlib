package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.datetime.Frequency;
import org.blacksmith.finlib.curve.node.CurveNode;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolatorFactory;

public class CurveFactory {
  public Curve createCurve(LocalDate valuationDate, CurveDefinition definition, List<CurveNodeReferenceData> referenceNodes) {
    ArgChecker.notEmpty(referenceNodes, "Reference nodes must be not empty");
    ArgChecker.isTrue(referenceNodes.size() > 2, "Reference nodes size must be greater than 2");
    List<CurveNode> nodes = referenceNodes.stream()
        .map(referenceNode -> createCurveNodeValue(valuationDate, definition, referenceNode))
        .sorted(Comparator.comparing(CurveNode::getX))
        .collect(Collectors.toList());
    var minKnot = Knot.of(nodes.get(0).getX(), nodes.get(0).getY());
    var maxKnot = Knot.of(nodes.get(nodes.size() - 1).getX(), nodes.get(nodes.size() - 1).getY());
    var xValues = nodes.stream().mapToDouble(CurveNode::getX).toArray();
    var yValues = nodes.stream().mapToDouble(CurveNode::getY).toArray();
    var function = (new InterpolatorFactory()).createFunction(definition.getInterpolator(), xValues, yValues);
    return new CurveImpl(definition.getCurveName(), valuationDate, definition.getDayCount(), function, minKnot, maxKnot);
  }

  private CurveNode createCurveNodeValue(LocalDate valuationDate, CurveDefinition definition, CurveNodeReferenceData referenceNode) {
    LocalDate nodeDate = (LocalDate) referenceNode.getTenor().addTo(valuationDate);
    double nodeXValue = definition.getDayCount().yearFraction(valuationDate, nodeDate);
    return CurveNode.of(nodeDate, nodeXValue, referenceNode.getValue(),
        CurveNodeMetadata.builder()
            .label(referenceNode.getLabel())
            .tenor(referenceNode.getTenor())
            .build());
  }
}
