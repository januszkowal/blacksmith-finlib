package org.blacksmith.finlib.curve.definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.node.CurveNodeDefinition;
import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;

public class CurveDefinitionBuilder {
  private String name;
  private Currency currency;
  private DayCount dayCount;
  private InterpolationAlgorithm interpolator;
  private List<CurveNodeDefinition> nodes = new ArrayList<>();

  public static CurveDefinitionBuilder builder() {
    return new CurveDefinitionBuilder();
  }

  public CurveDefinitionBuilder name(String name) {
    this.name = name;
    return this;
  }

  public CurveDefinitionBuilder currency(Currency currency) {
    this.currency = currency;
    return this;
  }

  public CurveDefinitionBuilder dayCount(DayCount dayCount) {
    this.dayCount = dayCount;
    return this;
  }

  public CurveDefinitionBuilder interpolator(InterpolationAlgorithm interpolator) {
    this.interpolator = interpolator;
    return this;
  }

  public CurveDefinitionBuilder nodes(Collection<CurveNodeDefinition> nodes) {
    this.nodes.addAll(nodes);
    return this;
  }

  public CurveDefinitionBuilder nodes(CurveNodeDefinition... nodes) {
    this.nodes.addAll(List.of(nodes));
    return this;
  }

  public CurveDefinition build() {
    return new CurveDefinition(name, currency, dayCount, interpolator, nodes);
  }
}
