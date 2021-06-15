package org.blacksmith.finlib.valuation.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CashflowsBuilder {
  private List<Cashflow> cashflows = new ArrayList<>();

  public CashflowsBuilder builder() {
    return new CashflowsBuilder();
  }

  public CashflowsBuilder cashflow(Cashflow cashflow) {
    this.cashflows.add(cashflow);
    return this;
  }

  public CashflowsBuilder cashflow(Collection<Cashflow> cashflows) {
    this.cashflows.addAll(cashflows);
    return this;
  }

  public List<Cashflow> build() {
    return cashflows;
  }
}
