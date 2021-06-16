package org.blacksmith.finlib.curve.definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.blacksmith.finlib.basic.currency.Currency;
import org.blacksmith.finlib.curve.node.CurveNodeDefinition;
import org.blacksmith.finlib.datetime.daycount.DayCount;
import org.blacksmith.finlib.math.analysis.interpolation.InterpolationAlgorithm;

public class CurveDefinitionBuilder {
  private String curveName;
  private Currency currency;
  private DayCount dayCount;
  private InterpolationAlgorithm interpolator;
  private List<CurveNodeDefinition> nodes = new ArrayList<>();

  public static CurveDefinitionBuilder builder() {
    return new CurveDefinitionBuilder();
  }

  public CurveDefinitionBuilder curveName(String curveName) {
    this.curveName = curveName;
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
    return new CurveDefinition(curveName, currency, dayCount, interpolator, nodes);
  }
}
