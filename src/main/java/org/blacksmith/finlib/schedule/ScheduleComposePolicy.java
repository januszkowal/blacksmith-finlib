package org.blacksmith.finlib.schedule;

import java.util.List;

import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleInterestEvent;

public interface ScheduleComposePolicy {
  List<CashflowInterestEvent> create(List<ScheduleInterestEvent> events);
  List<CashflowInterestEvent> update(List<CashflowInterestEvent> cashflows);
}
