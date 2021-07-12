package org.blacksmith.finlib.valuation.dcf;

import java.math.BigDecimal;
import java.util.List;

import org.blacksmith.finlib.basic.rounding.Rounding;
import org.blacksmith.finlib.curve.discount.CurveFactors;
import org.blacksmith.finlib.valuation.CashflowValuator;
import org.blacksmith.finlib.valuation.dto.Cashflow;

public class DcfValuator implements CashflowValuator {
  private final CurveFactors discountFactor;
  private final Rounding rounding;

  public DcfValuator(CurveFactors discountFactor, Rounding rounding) {
    this.discountFactor = discountFactor;
    this.rounding = rounding;
  }

  public static DcfValuator of (CurveFactors discountFactor, Rounding rounding) {
    return new DcfValuator(discountFactor, rounding);
  }

  @Override
  public BigDecimal value(List<Cashflow> cashflows) {
    return cashflows.stream()
        .map(cashflow -> valuate(cashflow))
        .reduce(BigDecimal.ZERO.ZERO, BigDecimal::add);
  }

  private BigDecimal valuate(Cashflow cashflow) {
    double dcf = discountFactor.discountFactor(cashflow.getDate());
    BigDecimal value = rounding.round(cashflow.getAmount().multiply(BigDecimal.valueOf(dcf)));
    return rounding.round(value);
  }
}
