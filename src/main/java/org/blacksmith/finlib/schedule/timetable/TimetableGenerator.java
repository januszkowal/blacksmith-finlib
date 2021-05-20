package org.blacksmith.finlib.schedule.timetable;

import java.util.List;

import org.blacksmith.finlib.schedule.ScheduleParameters;

/* Generates RAW schedule - only dates */
public interface TimetableGenerator {
  List<TimetableInterestEntry> generate(ScheduleParameters scheduleParameters);
}
