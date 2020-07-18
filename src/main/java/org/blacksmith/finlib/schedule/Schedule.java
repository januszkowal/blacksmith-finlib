package org.blacksmith.finlib.schedule;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@ToString
public class Schedule {
  private final List<XEvent> cashflows = new ArrayList<>();
  public List<XEvent> getCashflow() {
    return this.cashflows;
  }
}
