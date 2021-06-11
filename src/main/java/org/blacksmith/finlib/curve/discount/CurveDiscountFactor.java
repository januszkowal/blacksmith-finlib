package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

import org.blacksmith.finlib.curve.Curve;

public class CurveDiscountFactor {
  private final Curve curve;
  private final DiscountFactor discountFactor;

  public CurveDiscountFactor(Curve curve, DiscountFactor discountFactor) {
    this.curve = curve;
    this.discountFactor = discountFactor;
  }

  public static CurveDiscountFactor of(Curve curve, DiscountFactor discountFactor) {
    return new CurveDiscountFactor(curve, discountFactor);
  }

  public double discountFactor(LocalDate date) {
    double yearFraction = curve.getDayCount().relativeYearFraction(curve.getValuationDate(), date);
    double curveValue = curve.value(yearFraction);
    return discountFactor.discountFactor(yearFraction, curveValue);
  }
}
