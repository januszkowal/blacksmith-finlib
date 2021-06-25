package org.blacksmith.finlib.curve;

import java.util.Map;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.NamedItem;
import org.blacksmith.finlib.basic.currency.Currency;

import lombok.Value;

@Value
public class CurveGroup implements NamedItem {
  private final String name;
  private Map<Currency, Curve> curves;

  public CurveGroup(String name, Map<Currency, Curve> curves) {
    ArgChecker.notEmpty(name, "Curve Group Name must be not empty");
    ArgChecker.notEmpty(curves, "Curves must be not empty");
    this.name = name;
    this.curves = Map.copyOf(curves);
  }

  public CurveGroup of(String name,  Map<Currency, Curve> curves) {
    return new CurveGroup(name, curves);
  }

  public Curve getCurve(Currency currency) {
    return curves.get(currency);
  }
}
