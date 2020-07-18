package org.blacksmith.finlib.schedule.events;

import java.util.Collections;
import java.util.List;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleEventsGenerator;
import org.blacksmith.finlib.schedule.events.schedule.ScheduleInterestEvent;

public class TermScheduleEventsGenerator implements ScheduleEventsGenerator {

  @Override
  public List<ScheduleInterestEvent> generate(ScheduleParameters scheduleParameters) {
    return Collections.singletonList(ScheduleInterestEvent.builder()
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .paymentDateUnadjusted(scheduleParameters.getMaturityDate())
        .paymentDate(scheduleParameters.getMaturityDate())
        .build());
  }
}
