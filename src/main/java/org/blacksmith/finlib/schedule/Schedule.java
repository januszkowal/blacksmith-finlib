package org.blacksmith.finlib.schedule;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;
import org.blacksmith.finlib.basic.numbers.Amount;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.ScheduleEvent;

@ToString
public class Schedule {
  private final List<XEvent> cashflows = new ArrayList<>();
  public List<XEvent> getCashflow() {
    return this.cashflows;
  }
}
