package org.blacksmith.finlib.schedule.events.schedule;

import java.util.List;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

/* Generates RAW schedule - only dates */
public interface ScheduleEventsGenerator {
  List<ScheduleInterestEvent> generate(ScheduleParameters scheduleParameters);
}
