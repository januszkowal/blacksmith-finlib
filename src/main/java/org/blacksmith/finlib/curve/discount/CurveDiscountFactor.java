package org.blacksmith.finlib.curve.discount;

import java.time.LocalDate;

import org.blacksmith.commons.arg.ArgChecker;
import org.blacksmith.finlib.curve.Curve;

public class CurveDiscountFactor {
  private final Curve curve;
  private final DiscountFactor discountFactor;

  public CurveDiscountFactor(Curve curve, DiscountFactor discountFactor) {
    ArgChecker.notNull(curve, "Curve must be not null");
    ArgChecker.notNull(discountFactor, "Discount factor must be not null");
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

  public double forwardRate(LocalDate d1, LocalDate d2) {
    double dcf1 = discountFactor(d1);
    double dcf2 = discountFactor(d2);
    if (dcf2 == 0)
      return 0d;
    return (dcf1 / dcf2) * (1 / curve.getDayCount().yearFraction(d1, d2));
  }
}
