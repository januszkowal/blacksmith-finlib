package org.blacksmith.finlib.schedule.policy;

import java.util.List;

import org.blacksmith.finlib.schedule.events.InterestEvent;
import org.blacksmith.finlib.schedule.timetable.TimetableInterestEntry;

public interface ScheduleAlgorithm {
  List<InterestEvent> create(List<TimetableInterestEntry> events);
  List<InterestEvent> update(List<InterestEvent> cashflows);
}
