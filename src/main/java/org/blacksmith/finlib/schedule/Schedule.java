package org.blacksmith.finlib.schedule;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@ToString
public class Schedule {
  private List<Cashflow> cashflows = new ArrayList<>();
  
  public List<Cashflow> getCashflow() {
    return this.cashflows;
  }
}
