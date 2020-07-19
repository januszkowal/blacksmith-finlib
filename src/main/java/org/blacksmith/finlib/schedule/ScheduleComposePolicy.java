package org.blacksmith.finlib.schedule;

import java.util.List;

import org.blacksmith.finlib.schedule.events.interest.CashflowInterestEvent;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;

public interface ScheduleComposePolicy {
  List<CashflowInterestEvent> create(List<TimetableInterestEntry> events);
  List<CashflowInterestEvent> update(List<CashflowInterestEvent> cashflows);
}
