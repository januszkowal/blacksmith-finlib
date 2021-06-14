package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.definition.CurveDefinition;
import org.blacksmith.finlib.curve.node.CurveNode;
import org.blacksmith.finlib.curve.node.CurveNodeMetadata;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;
import org.blacksmith.finlib.curve.node.SimpleCurveNodeReferenceData;
import org.blacksmith.finlib.curve.types.Knot;
import org.blacksmith.finlib.marketdata.QuoteProvider;
import org.blacksmith.finlib.math.analysis.InterpolatorFactory;

public class CurveFactory {
  private final QuoteProvider quoteProvider;

  public CurveFactory(QuoteProvider quoteProvider) {
    this.quoteProvider = quoteProvider;
  }

  public Curve createCurve(LocalDate valuationDate, CurveDefinition definition) {
    ArgChecker.notNull(valuationDate, "Valuation date must be not null");
    ArgChecker.notNull(definition, "Definition be not null");

    List<CurveNodeReferenceData> referenceNodes = definition.getNodes().stream()
        .map(node -> SimpleCurveNodeReferenceData
            .of(node.getLabel(), node.getTenor(), quoteProvider.getQuote(valuationDate, node.getQuoteId()) + node.getSpread()))
        .collect(Collectors.toList());

    return createCurve(valuationDate, definition, referenceNodes);
  }

  public Curve createCurve(LocalDate valuationDate, CurveDefinition definition, List<CurveNodeReferenceData> referenceNodes) {
    ArgChecker.notNull(valuationDate, "Valuation date must be not null");
    ArgChecker.notNull(definition, "Definition be not null");
    ArgChecker.isTrue(referenceNodes.size() >= CurveDefinition.MIN_NODES_SIZE, "Reference nodes size must be greater than " + CurveDefinition.MIN_NODES_SIZE);
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
