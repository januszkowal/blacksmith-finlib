package org.blacksmith.finlib.curve.definition;

import java.util.Map;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.basic.currency.Currency;

import lombok.Value;

@Value
public class CurveDefinitionGroup {
  String name;
  Map<Currency, String> curves;

  public CurveDefinitionGroup(String name, Map<Currency, String> curves) {
    ArgChecker.notEmpty(name);
    ArgChecker.notEmpty(curves);
    this.name = name;
    this.curves = Map.copyOf(curves);
  }
}
