package org.blacksmith.finlib.interest.schedule.timetable;

import java.util.List;

import org.blacksmith.finlib.interest.schedule.ScheduleParameters;

/* Generates RAW schedule - only dates */
public interface TimetableGenerator {
  List<TimetableInterestEntry> generate(ScheduleParameters scheduleParameters);
}
