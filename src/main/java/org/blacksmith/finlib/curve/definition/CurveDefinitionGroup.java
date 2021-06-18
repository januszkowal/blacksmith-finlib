package org.blacksmith.finlib.curve.definition;

import java.util.Map;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class CurveDefinitionGroup {
  String name;
  @Singular(value="curve")
  Map<Currency, String> curves;

  public CurveDefinitionGroup(String name, Map<Currency, String> curves) {
    ArgChecker.notEmpty(name, "Curve group must be not empty");
    ArgChecker.notEmpty(curves, "Group must contain curves");
    this.name = name;
    this.curves = Map.copyOf(curves);
  }
}
