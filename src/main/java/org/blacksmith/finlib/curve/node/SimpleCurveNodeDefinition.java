package org.blacksmith.finlib.curve.node;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.datetime.Tenor;
import org.blacksmith.finlib.marketdata.QuoteId;

import lombok.Value;

@Value
public class SimpleCurveNodeDefinition implements CurveNodeDefinition {
  private final String label;
  private final Tenor tenor;
  private final QuoteId quoteId;
  private final double spread;

  private SimpleCurveNodeDefinition(String label, Tenor tenor, QuoteId quoteId, double spread) {
    ArgChecker.notEmpty(label, "Label must be not empty/null");
    ArgChecker.notNull(tenor, "Tenor must be not null");
    ArgChecker.notNull(quoteId, "QuoteID must be not null");
    this.label = label;
    this.tenor = tenor;
    this.quoteId = quoteId;
    this.spread = spread;
  }

  public static SimpleCurveNodeDefinition of(String label, Tenor tenor, QuoteId quoteId, double spread) {
    return new SimpleCurveNodeDefinition(label, tenor, quoteId, spread);
  }
}
