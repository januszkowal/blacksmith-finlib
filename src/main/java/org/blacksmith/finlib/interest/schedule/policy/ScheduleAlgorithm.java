package org.blacksmith.finlib.interest.schedule.policy;

import java.util.List;

import org.blacksmith.finlib.interest.schedule.events.InterestEvent;
import org.blacksmith.finlib.interest.schedule.timetable.TimetableInterestEntry;

public interface ScheduleAlgorithm {
  List<InterestEvent> create(List<TimetableInterestEntry> events);

  List<InterestEvent> update(List<InterestEvent> cashflows);
}
