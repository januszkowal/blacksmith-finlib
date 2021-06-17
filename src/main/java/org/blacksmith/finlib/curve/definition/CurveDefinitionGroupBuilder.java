package org.blacksmith.finlib.curve.definition;

import java.util.HashMap;
import java.util.Map;

import org.blacksmith.finlib.basic.currency.Currency;

public class CurveDefinitionGroupBuilder {
  private String name;
  private Map<Currency, String> curves = new HashMap<>();

  public static CurveDefinitionGroupBuilder builder() {
    return new CurveDefinitionGroupBuilder();
  }

  public CurveDefinitionGroupBuilder name(String name) {
    this.name = name;
    return this;
  }

  public CurveDefinitionGroupBuilder curve(Currency currency, String name) {
    this.curves.put(currency, name);
    return this;
  }

  public CurveDefinitionGroup build() {
    return new CurveDefinitionGroup(name, curves);
  }
}
