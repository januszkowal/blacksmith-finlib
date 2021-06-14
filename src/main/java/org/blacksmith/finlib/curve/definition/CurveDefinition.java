package org.blacksmith.finlib.curve.definition;

import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.node.CurveNodeDefinition;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.datetime.daycount.DayCount;

import lombok.Value;

@Value(staticConstructor = "of")
public class CurveDefinition {
  public static final int MIN_NODES_SIZE = 3;
  private final String curveName;
  private final Currency currency;
  private final DayCount dayCount;
  private final InterpolationAlgorithm interpolator;
  private final List<CurveNodeDefinition> nodes;

  public CurveDefinition(String curveName, Currency currency, DayCount dayCount, InterpolationAlgorithm interpolator,
      List<CurveNodeDefinition> nodes) {
    ArgChecker.notEmpty(curveName, "Curve name must be not empty");
    ArgChecker.notNull(currency, "Currency must be not null");
    ArgChecker.notNull(dayCount, "Day count must be not null");
    ArgChecker.notNull(interpolator, "Interpolator must be not null");
    ArgChecker.notNull(nodes, "Nodes must be not null");
    ArgChecker.isTrue(nodes.size() >= MIN_NODES_SIZE, "Nodes size must be greater than " + MIN_NODES_SIZE);
    this.curveName = curveName;
    this.dayCount = dayCount;
    this.currency = currency;
    this.interpolator = interpolator;
    this.nodes = nodes;
  }
}
