package org.blacksmith.finlib.schedule.events;

import java.util.List;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public interface ScheduleEventsGenerator {
  List<ScheduleEvent> generate(ScheduleParameters scheduleParameters);
}
