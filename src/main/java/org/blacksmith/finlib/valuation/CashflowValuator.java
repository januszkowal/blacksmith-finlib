package org.blacksmith.finlib.valuation;

import java.math.BigDecimal;
import java.util.List;

import org.blacksmith.finlib.valuation.dto.Cashflow;

public interface CashflowValuator {
  BigDecimal value(List<Cashflow> cashflows);
}
