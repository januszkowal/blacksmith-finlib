package org.blacksmith.finlib.schedule.events;

import java.util.Collections;
import java.util.List;
import org.blacksmith.finlib.interestbasis.ScheduleParameters;

public class TermScheduleEventsGenerator implements ScheduleEventsGenerator {

  @Override
  public List<ScheduleEvent> generate(ScheduleParameters scheduleParameters) {
    return Collections.singletonList(ScheduleEvent.builder()
        .startDate(scheduleParameters.getStartDate())
        .endDate(scheduleParameters.getMaturityDate())
        .paymentDateUnadjusted(scheduleParameters.getMaturityDate())
        .paymentDate(scheduleParameters.getMaturityDate())
        .build());
  }
}
