package org.blacksmith.finlib.curve.definition;

import java.util.List;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.NamedItem;
import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.node.CurveNodeDefinition;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;
import org.blacksmith.finlib.datetime.daycount.DayCount;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class CurveDefinition implements NamedItem {
  public static final int MIN_NODES_SIZE = 3;
  private final String name;
  private final Currency currency;
  private final DayCount dayCount;
  private final InterpolationAlgorithm interpolator;
  @Singular
  private final List<CurveNodeDefinition> nodes;

  public CurveDefinition(String name, Currency currency, DayCount dayCount, InterpolationAlgorithm interpolator,
      List<CurveNodeDefinition> nodes) {
    ArgChecker.notEmpty(name, "Curve name must be not empty");
    ArgChecker.notNull(currency, "Currency must be not null");
    ArgChecker.notNull(dayCount, "Day count must be not null");
    ArgChecker.notNull(interpolator, "Interpolator must be not null");
    ArgChecker.notNull(nodes, "Nodes must be not null");
    ArgChecker.isTrue(nodes.size() >= MIN_NODES_SIZE, "Nodes size must be greater than " + MIN_NODES_SIZE);
    this.name = name;
    this.dayCount = dayCount;
    this.currency = currency;
    this.interpolator = interpolator;
    this.nodes = nodes;
  }
}
