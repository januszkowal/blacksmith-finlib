package org.blacksmith.finlib.curve;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.definition.CurveDefinition;
import org.blacksmith.finlib.curve.node.CurveNodeReferenceData;
import org.blacksmith.finlib.curve.node.SimpleCurveNodeReferenceData;
import org.blacksmith.finlib.marketdata.QuoteProvider;

public class CurveFactoryDef {
  private final QuoteProvider quoteProvider;
  private final CurveFactory curveFactory = new CurveFactory();

  public CurveFactoryDef(QuoteProvider quoteProvider) {
    ArgChecker.notNull(quoteProvider, "Quote provider must be not null");
    this.quoteProvider = quoteProvider;
  }

  public Curve createCurve(LocalDate valuationDate, CurveDefinition definition) {
    ArgChecker.notNull(valuationDate, "Valuation date must be not null");
    ArgChecker.notNull(definition, "Definition be not null");

    List<CurveNodeReferenceData> referenceNodes = definition.getNodes().stream()
        .map(node -> SimpleCurveNodeReferenceData
            .of(node.getLabel(), node.getTenor(), quoteProvider.getQuote(valuationDate, node.getQuoteId()) + node.getSpread()))
        .collect(Collectors.toList());

    return curveFactory.createCurve(valuationDate, definition, referenceNodes);
  }
}
